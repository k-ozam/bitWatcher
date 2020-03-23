package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.repository.ExchangeRateRepository
import jp.maskedronin.bitwatcher.domain.repository.PropertyRepository
import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

private const val RATE_REFRESH_INTERVAL_MIN_SECONDS = 30L

@Singleton
class RefreshExchangeRateUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val propertyRepository: PropertyRepository,
    private val settingsRepository: SettingsRepository
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
        val refreshInterval: Long = settingsRepository.getRateRefreshIntervalSeconds()?.toLong()
            ?: RATE_REFRESH_INTERVAL_MIN_SECONDS

        val currency: Currency = settingsRepository.getSettlementCurrency()
            .filterNotNull()
            .first()

        propertyRepository.getAllProperties()
            .first()
            .filter { property ->
                val lastUpdatedAt = exchangeRateRepository.getRate(
                    property.currency,
                    currency,
                    property.exchange
                )?.updatedAt

                // インターバルが指定時間より短い場合は価格更新しない
                return@filter lastUpdatedAt
                    ?.plusSeconds(refreshInterval)
                    ?.isAfter(LocalDateTime.now()) != true
            }
            .map { property ->
                coroutineScope {
                    async {
                        exchangeRateRepository.refreshRate(
                            property.currency,
                            currency,
                            property.exchange
                        )
                    }
                }
            }
            .forEach { deferred ->
                deferred.await()
            }
    }
}