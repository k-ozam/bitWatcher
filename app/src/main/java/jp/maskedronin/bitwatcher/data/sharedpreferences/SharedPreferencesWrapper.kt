package jp.maskedronin.bitwatcher.data.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit
import jp.maskedronin.bitwatcher.data.extension.getNullableEnumAsFlow
import jp.maskedronin.bitwatcher.data.extension.putEnum
import jp.maskedronin.bitwatcher.di.Encrypt
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesWrapper @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @Encrypt
    private val encryptSharedPreferences: SharedPreferences
) {
    fun setRateRefreshIntervalSeconds(seconds: Int) {
        sharedPreferences.edit(commit = true) {
            putInt(
                SharedPreferencesElement.RATE_REFRESH_INTERVAL_SECONDS.key,
                seconds
            )
        }
    }

    fun getRateRefreshIntervalSeconds(): Int? = sharedPreferences.getInt(
        SharedPreferencesElement.RATE_REFRESH_INTERVAL_SECONDS.key, Int.MIN_VALUE
    ).takeUnless { it == Int.MIN_VALUE }

    fun getSettlementCurrency(): Flow<Currency?> =
        sharedPreferences.getNullableEnumAsFlow<Currency>(
            SharedPreferencesElement.SETTLEMENT_CURRENCY.key
        )

    fun setSettlementCurrency(currency: Currency) {
        sharedPreferences.edit(commit = true) {
            putEnum(SharedPreferencesElement.SETTLEMENT_CURRENCY.key, currency)
        }
    }

    fun isOnboardingCompleted(): Boolean = sharedPreferences.getBoolean(
        SharedPreferencesElement.IS_ONBOARDING_COMPLETED.key,
        false
    )

    fun setOnboardingCompleted(completed: Boolean) {
        sharedPreferences.edit(commit = true) {
            putBoolean(
                SharedPreferencesElement.IS_ONBOARDING_COMPLETED.key,
                completed
            )
        }
    }

    fun getDatabasePassphrase(): String? = encryptSharedPreferences.getString(
        SharedPreferencesElement.DATABASE_PASSPHRASE.key, null
    )

    fun setDatabasePassphrase(passphrase: String) {
        encryptSharedPreferences.edit(commit = true) {
            putString(SharedPreferencesElement.DATABASE_PASSPHRASE.key, passphrase)
        }
    }
}