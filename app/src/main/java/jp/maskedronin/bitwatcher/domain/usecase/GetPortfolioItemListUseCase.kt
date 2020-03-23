package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.entity.ExchangeRate
import jp.maskedronin.bitwatcher.domain.repository.ExchangeRateRepository
import jp.maskedronin.bitwatcher.domain.repository.PropertyRepository
import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository
import jp.maskedronin.bitwatcher.presentation.portfolio.PortfolioItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPortfolioItemListUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<List<PortfolioItem>> = combine(
        propertyRepository.getAllProperties(),
        settingsRepository.getSettlementCurrency().filterNotNull(),
        exchangeRateRepository.getAllRates()
    ) { propertyList, Currency, rateList ->
        propertyList.map { property ->
            val rate: ExchangeRate? = rateList.find { rate ->
                rate.base == property.currency &&
                        rate.quote == Currency &&
                        rate.exchange == property.exchange
            }

            PortfolioItem(
                currency = property.currency,
                settlement = Currency,
                exchange = property.exchange,
                amount = property.amount,
                rate = rate?.value,
                amountUpdatedAt = property.updatedAt,
                rateUpdatedAt = rate?.updatedAt,
                isLatestRate = rate?.isLatest ?: false
            )
        }.sortedBy {
            it.amount
        }
    }
}