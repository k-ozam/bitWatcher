package jp.maskedronin.bitwatcher.data.api.bitbank

import kotlinx.serialization.Serializable

@Serializable
data class BitbankTickerWrapper(
    val success: Int,
    val data: BitbankTicker
)

@Serializable
data class BitbankTicker(
    val sell: Double,
    val buy: Double,
    val high: Double,
    val low: Double,
    val last: Double,
    val vol: Double,
    val timestamp: Long
)