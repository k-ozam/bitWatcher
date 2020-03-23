package jp.maskedronin.bitwatcher.data.api.poloniex

import retrofit2.http.GET

interface PoloniexApi {
    companion object {
        const val ENDPOINT = "https://poloniex.com/"
    }

    /**
     * @return e.g. key=ETH_BTC
     */
    @Deprecated("invoke through PoloniexTickerApiWrapper")
    @GET("/public?command=returnTicker")
    suspend fun getTicker(): Map<String, PoloniexTicker>
}