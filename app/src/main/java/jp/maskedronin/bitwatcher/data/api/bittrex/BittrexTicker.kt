package jp.maskedronin.bitwatcher.data.api.bittrex

import kotlinx.serialization.Serializable

@Serializable
data class BittrexTicker(
    val symbol: String,
    val lastTradeRate: Double,
    val bidRate: Double,
    val askRate: Double
)