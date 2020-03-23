package jp.maskedronin.bitwatcher.data.api.cryptowatch

import kotlinx.serialization.Serializable

@Serializable
data class CryptowatchPair(
    val symbol: String,
    val id: Int,
    val base: Base,
    val quote: Quote,
    val route: String,
    val markets: List<Market>
)

@Serializable
data class Allowance(
    val cost: Int,
    val remaining: Long,
    val remainingPaid: Long,
    val upgrade: String
)

@Serializable
data class Base(
    val id: Int,
    val symbol: String,
    val name: String,
    val fiat: Boolean,
    val route: String
)

@Serializable
data class Market(
    val id: Int,
    val exchange: String,
    val pair: String,
    val active: Boolean,
    val route: String
)

@Serializable
data class Quote(
    val id: Int,
    val symbol: String,
    val name: String,
    val fiat: Boolean,
    val route: String
)

@Serializable
data class CryptowatchPairWrapper(
    val result: CryptowatchPair,
    val allowance: Allowance
)