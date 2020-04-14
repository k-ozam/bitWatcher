package jp.maskedronin.bitwatcher.data.api.bitbank

import retrofit2.http.GET
import retrofit2.http.Path

interface BitbankApi {
    companion object {
        const val ENDPOINT = "https://public.bitbank.cc/"
    }

    @GET("/{pair}/ticker")
    suspend fun getTicker(
        @Path("pair") pair: String
    ): BitbankTickerWrapper
}