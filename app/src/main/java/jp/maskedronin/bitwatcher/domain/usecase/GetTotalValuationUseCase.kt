package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.repository.ExchangeRateRepository
import jp.maskedronin.bitwatcher.domain.repository.PropertyRepository
import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTotalValuationUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Double> = combine(
        propertyRepository.getAllProperties(),
        exchangeRateRepository.getAllRates(),
        settingsRepository.getSettlementCurrency()
    ) { properties, rates, Currency ->
        properties.map { property ->
            val rate: Double = rates.find { rate ->
                property.currency == rate.base &&
                        property.exchange == rate.exchange &&
                        Currency == rate.quote
            }?.value ?: 0.0
            rate * property.amount
        }.sum()
    }
}