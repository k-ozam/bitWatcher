package jp.maskedronin.bitwatcher.data.api.binancejex

import kotlinx.serialization.Serializable

@Serializable
data class BinanceJexTicker(
    val id: Int,
    val last: Double,
    val lowestAsk: Double,
    val highestBid: Double,
    val percentChange: Double,
    val baseVolume: Double,
    val quoteVolume: Double,
    val isFrozen: Int,
    val high24hr: Double,
    val low24hr: Double,
    val baseAssetName: String,
    val quoteAssetName: String
)