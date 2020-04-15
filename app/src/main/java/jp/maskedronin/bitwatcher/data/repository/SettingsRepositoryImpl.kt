package jp.maskedronin.bitwatcher.data.repository

import jp.maskedronin.bitwatcher.data.sharedpreferences.SharedPreferencesWrapper
import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val sharedPreferencesWrapper: SharedPreferencesWrapper
) : SettingsRepository {
    override suspend fun setRateRefreshIntervalSeconds(seconds: Int) {
        sharedPreferencesWrapper.setRateRefreshIntervalSeconds(seconds)
    }

    override fun getRateRefreshIntervalSeconds(): Int? =
        sharedPreferencesWrapper.getRateRefreshIntervalSeconds()

    override fun getSettlementCurrency(): Flow<Currency?> =
        sharedPreferencesWrapper.getSettlementCurrency()

    override suspend fun setSettlementCurrency(currency: Currency) {
        require(Currency.settlements.contains(currency))
        sharedPreferencesWrapper.setSettlementCurrency(currency)
    }

    override fun isOnboardingCompleted(): Boolean =
        sharedPreferencesWrapper.isOnboardingCompleted()

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        sharedPreferencesWrapper.setOnboardingCompleted(completed)
    }

    override fun isSwipeRefreshTutorialEnabled(): Flow<Boolean?> =
        sharedPreferencesWrapper.isSwipeRefreshTutorialEnabled()

    override suspend fun setSwipeRefreshTutorialEnabled(enabled: Boolean) {
        sharedPreferencesWrapper.setSwipeRefreshTutorialEnabled(enabled)
    }
}