package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.repository.ExchangeAccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetExchangeAccountUseCase @Inject constructor(
    private val exchangeAccountRepository: ExchangeAccountRepository
) {
    suspend operator fun invoke(account: ExchangeAccount) {
        exchangeAccountRepository.setAccount(account)
    }
}