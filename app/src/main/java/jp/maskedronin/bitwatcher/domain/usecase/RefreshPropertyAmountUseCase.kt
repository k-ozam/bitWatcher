package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.repository.ExchangeAccountRepository
import jp.maskedronin.bitwatcher.domain.repository.PropertyRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.common.exception.ExchangeApiUnauthorizedException
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import org.threeten.bp.LocalDateTime
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

private const val AMOUNT_REFRESH_INTERVAL_MIN_SECONDS = 60L

@Singleton
class RefreshPropertyAmountUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val exchangeAccountRepository: ExchangeAccountRepository
) {
    private val _isProcessing = BroadcastChannel<Boolean>(Channel.CONFLATED)
        .apply { offer(false) }
    val isProcessing: Flow<Boolean> = _isProcessing.asFlow()

    suspend operator fun invoke() {
        if (isProcessing.first()) {
            return
        }

        _isProcessing.offer(true)

        runCatching {
            refresh()
        }.onSuccess {
            _isProcessing.offer(false)
        }.onFailure { throwable ->
            _isProcessing.offer(false)

            throw throwable
        }
    }

    private suspend fun refresh() {
        Exchange.values()
            .filter(Exchange::isBalanceApiAvailable)
            .filter { exchange ->
                val account: ExchangeAccount? = exchangeAccountRepository.getAccount(exchange)
                when {
                    account == null -> false
                    account.isValid == null -> true
                    account.isValid -> true
                    account.isValid.not() -> true // false
                    else -> error("unknown state: account=$account")
                }
            }
            .filter { exchange ->
                // インターバルが指定時間より短い場合は更新しない
                exchangeAccountRepository.getAccount(exchange)!!
                    .updatedAt
                    ?.plusSeconds(AMOUNT_REFRESH_INTERVAL_MIN_SECONDS)
                    ?.isAfter(LocalDateTime.now()) != true
            }
            .also { exchanges ->
                coroutineScope {
                    exchanges.map { exchange ->
                        async {
                            runCatching {
                                propertyRepository.refreshProperties(exchange)
                            }.onSuccess {
                                val newAccount = exchangeAccountRepository.getAccount(exchange)!!
                                    .copy(isValid = true, updatedAt = LocalDateTime.now())
                                exchangeAccountRepository.setAccount(newAccount)
                            }.onFailure { throwable ->
                                val newAccount = exchangeAccountRepository.getAccount(exchange)!!
                                    .copy(isValid = false)
                                exchangeAccountRepository.setAccount(newAccount)

                                throw if (throwable is HttpException && throwable.code() == 401) {
                                    ExchangeApiUnauthorizedException(
                                        exchange,
                                        throwable
                                    )
                                } else throwable
                            }
                        }
                    }
                }.forEach { deferred ->
                    deferred.await()
                }
            }
    }
}