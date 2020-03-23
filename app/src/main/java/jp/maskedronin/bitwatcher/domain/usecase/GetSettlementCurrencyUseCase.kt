package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSettlementCurrencyUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Currency?> = settingsRepository.getSettlementCurrency()
}