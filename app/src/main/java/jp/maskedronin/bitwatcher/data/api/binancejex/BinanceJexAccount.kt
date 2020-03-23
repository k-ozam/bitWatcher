package jp.maskedronin.bitwatcher.data.api.binancejex

import kotlinx.serialization.Serializable

@Serializable
data class BinanceJexAccount(
    val updateTime: Long,
    val spotBalances: List<BinanceJexBalance>
)

@Serializable
data class BinanceJexBalance(
    val asset: String,
    val free: Double,
    val locked: Double
)