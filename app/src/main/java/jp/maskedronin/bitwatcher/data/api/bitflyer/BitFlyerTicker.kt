package jp.maskedronin.bitwatcher.data.api.bitflyer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BitFlyerTicker(
    @SerialName("product_code")
    val productCode: String,
    val timestamp: String,
    @SerialName("tick_id")
    val tickId: Int,
    @SerialName("best_bid")
    val bestBid: Double,
    @SerialName("best_ask")
    val bestAsk: Double,
    @SerialName("best_bid_size")
    val bestBidSize: Double,
    @SerialName("best_ask_size")
    val bestAskSize: Double,
    @SerialName("total_bid_depth")
    val totalBidDepth: Double,
    @SerialName("total_ask_depth")
    val totalAskDepth: Double,
    val ltp: Double,
    val volume: Double,
    @SerialName("volume_by_product")
    val volumeByProduct: Double
)