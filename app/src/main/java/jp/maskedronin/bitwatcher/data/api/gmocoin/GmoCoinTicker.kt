package jp.maskedronin.bitwatcher.data.api.gmocoin

import kotlinx.serialization.Serializable

@Serializable
data class GmoCoinTicker(
    val ask: Double,
    val bid: Double,
    val high: Double,
    val last: Double,
    val low: Double,
    val symbol: String,
    val timestamp: String,
    val volume: Double
)

@Serializable
data class GmoCoinTickerWrapper(
    val status: Int,
    val data: List<GmoCoinTicker>,
    val responsetime: String
)
