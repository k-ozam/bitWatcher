package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetSettlementCurrencyUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(Currency: Currency) {
        settingsRepository.setSettlementCurrency(Currency)
    }
}