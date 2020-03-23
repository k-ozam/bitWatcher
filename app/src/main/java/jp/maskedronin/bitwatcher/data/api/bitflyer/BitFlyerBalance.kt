package jp.maskedronin.bitwatcher.data.api.bitflyer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitFlyerBalance(
    @SerialName("currency_code")
    val currencyCode: String,
    val amount: Double,
    val available: Double
)