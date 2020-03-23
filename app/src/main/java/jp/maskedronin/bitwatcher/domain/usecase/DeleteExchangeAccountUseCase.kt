package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.repository.ExchangeAccountRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteExchangeAccountUseCase @Inject constructor(
    private val exchangeAccountRepository: ExchangeAccountRepository
) {
    suspend operator fun invoke(exchange: Exchange) {
        exchangeAccountRepository.deleteAccount(exchange)
    }
}