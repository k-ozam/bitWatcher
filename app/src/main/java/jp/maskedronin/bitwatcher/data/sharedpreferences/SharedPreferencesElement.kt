package jp.maskedronin.bitwatcher.data.sharedpreferences

import java.util.*

enum class SharedPreferencesElement {
    RATE_REFRESH_INTERVAL_SECONDS,
    SETTLEMENT_CURRENCY,
    IS_ONBOARDING_COMPLETED,
    DATABASE_PASSPHRASE,
    IS_SWIPE_REFRESH_TUTORIAL_ENABLED,
    ;

    val key: String
        get() = name.toLowerCase(Locale.US)
}