package jp.maskedronin.bitwatcher.data.api.binance

import kotlinx.serialization.Serializable

@Serializable
data class BinanceUserInfo(
    val balances: List<BinanceBalance>
)

@Serializable
data class BinanceBalance(
    val asset: String,
    val free: Double,
    val locked: Double
)