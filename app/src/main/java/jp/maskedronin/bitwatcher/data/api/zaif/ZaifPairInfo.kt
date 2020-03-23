package jp.maskedronin.bitwatcher.data.api.zaif

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ZaifPairInfo(
    val name: String,
    val title: String,
    @SerialName("currency_pair")
    val currencyPair: String,
    val description: String,
    @SerialName("is_token")
    val isToken: Boolean,
    @SerialName("event_number")
    val eventNumber: Int,
    val seq: Int,
    @SerialName("item_unit_min")
    val itemUnitMin: Float,
    @SerialName("item_unit_step")
    val itemUnitStep: Float,
    @SerialName("item_japanese")
    val itemJapanese: String,
    @SerialName("aux_unit_min")
    val auxUnitMin: Float,
    @SerialName("aux_unit_step")
    val auxUnitStep: Float,
    @SerialName("aux_unit_point")
    val auxUnitPoint: Int,
    @SerialName("aux_japanese")
    val auxJapanese: String
)