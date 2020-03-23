package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.repository.ExchangeAccountRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetExchangeAccountUseCase @Inject constructor(
    private val exchangeAccountRepository: ExchangeAccountRepository
) {
    suspend operator fun invoke(exchange: Exchange): ExchangeAccount? =
        exchangeAccountRepository.getAccountAsFlow(exchange).first()
}