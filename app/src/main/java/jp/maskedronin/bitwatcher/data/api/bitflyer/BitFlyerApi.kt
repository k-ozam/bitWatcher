package jp.maskedronin.bitwatcher.data.api.bitflyer

import jp.maskedronin.bitwatcher.data.api.RequireAuth
import retrofit2.http.GET
import retrofit2.http.Query

interface BitFlyerApi {
    companion object {
        const val ENDPOINT = "https://api.bitflyer.com/"
    }

    /**
     * @param productCode BTC_JPY, ETH_BTC, BCH_BTC, ETH_JPY
     */
    @GET("/v1/ticker")
    suspend fun getTicker(
        @Query("product_code") productCode: String
    ): BitFlyerTicker

    @RequireAuth
    @GET("/v1/me/getbalance")
    suspend fun getBalanceList(): List<BitFlyerBalance>
}