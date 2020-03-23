package jp.maskedronin.bitwatcher.domain.repository

import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun setRateRefreshIntervalSeconds(seconds: Int)
    fun getRateRefreshIntervalSeconds(): Int?

    fun getSettlementCurrency(): Flow<Currency?>
    suspend fun setSettlementCurrency(currency: Currency)

    fun isOnboardingCompleted(): Boolean
    suspend fun setOnboardingCompleted(completed: Boolean)
}