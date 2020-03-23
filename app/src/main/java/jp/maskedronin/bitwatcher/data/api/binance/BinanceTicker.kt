package jp.maskedronin.bitwatcher.data.api.binance

import kotlinx.serialization.Serializable

@Serializable
data class BinanceTicker(
    val symbol: String,
    val price: Double
)