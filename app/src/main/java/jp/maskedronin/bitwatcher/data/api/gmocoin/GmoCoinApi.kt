package jp.maskedronin.bitwatcher.data.api.gmocoin

import retrofit2.http.GET
import retrofit2.http.Query

interface GmoCoinApi {
    companion object {
        const val ENDPOINT = "https://api.coin.z.com/"
    }

    /**
     * @param symbol BTC_JPY, ETH_JPY, BCH_JPY, LTC_JPY, XRP_JPY
     */
    @GET("/public/v1/ticker")
    suspend fun getTicker(
        @Query("symbol") symbol: String
    ): GmoCoinTickerWrapper
}