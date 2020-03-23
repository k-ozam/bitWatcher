package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.repository.ExchangeAccountRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllExchangeAccountsUseCase @Inject constructor(
    private val exchangeAccountRepository: ExchangeAccountRepository
) {
    operator fun invoke(): Flow<List<ExchangeAccount>> =
        Exchange.values().filter(Exchange::isBalanceApiAvailable)
            .map { exchangeAccountRepository.getAccountAsFlow(it) }
            .let { flows ->
                combine(flows) { it.filterNotNull() }
            }
}