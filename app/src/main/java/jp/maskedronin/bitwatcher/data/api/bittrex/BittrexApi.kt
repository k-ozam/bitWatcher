package jp.maskedronin.bitwatcher.data.api.bittrex

import retrofit2.http.GET
import retrofit2.http.Path

interface BittrexApi {
    companion object {
        const val ENDPOINT = "https://api.bittrex.com/"
    }

    /**
     * @param marketSymbol e.g. BTC-USD, ETH-BTC
     */
    @GET("/v3/markets/{marketSymbol}/ticker")
    suspend fun getTicker(
        @Path("marketSymbol")
        marketSymbol: String
    ): BittrexTicker
}