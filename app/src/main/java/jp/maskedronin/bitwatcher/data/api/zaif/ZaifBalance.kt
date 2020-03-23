package jp.maskedronin.bitwatcher.data.api.zaif

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ZaifBalanceResponse(
    val success: Int,
    @SerialName("return")
    val zaifBalanceWrapper: ZaifBalanceWrapper
)

@Serializable
data class ZaifBalanceWrapper(
    val funds: Map<String, Double>
)