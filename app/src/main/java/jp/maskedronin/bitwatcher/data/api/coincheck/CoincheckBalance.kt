package jp.maskedronin.bitwatcher.data.api.coincheck

import kotlinx.serialization.Serializable

@Serializable
data class CoincheckBalance(
    val success: Boolean,
    val jpy: Double,
    val btc: Double,
    val eth: Double,
    val etc: Double,
    val lsk: Double,
    val fct: Double,
    val xrp: Double,
    val xem: Double,
    val ltc: Double,
    val bch: Double,
    val mona: Double,
    val xlm: Double,
    val qtum: Double
)