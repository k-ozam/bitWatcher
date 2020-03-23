package jp.maskedronin.bitwatcher.data.api.zaif

import kotlinx.serialization.Serializable

@Serializable
data class ZaifTicker(
    val last: Double,
    val high: Double?,
    val low: Double?,
    val vwap: Double?,
    val volume: Double?,
    val bid: Double,
    val ask: Double
)