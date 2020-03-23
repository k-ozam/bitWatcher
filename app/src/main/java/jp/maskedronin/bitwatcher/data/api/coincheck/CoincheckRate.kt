package jp.maskedronin.bitwatcher.data.api.coincheck

import kotlinx.serialization.Serializable

@Serializable
data class CoincheckRate(
    val success: Boolean,
    val rate: Double,
    val price: Double,
    val amount: Double
)