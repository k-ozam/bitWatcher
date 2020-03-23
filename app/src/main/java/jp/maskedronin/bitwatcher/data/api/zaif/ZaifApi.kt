package jp.maskedronin.bitwatcher.data.api.zaif

import jp.maskedronin.bitwatcher.data.api.RequireAuth
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ZaifApi {
    companion object {
        const val ENDPOINT = "https://api.zaif.jp/"
    }

    @GET("/api/1/ticker/{currency_pair}")
    suspend fun getTicker(
        @Path("currency_pair")
        currencyPair: String
    ): ZaifTicker

    @RequireAuth
    @ZaifApiMethod("get_info2")
    @POST("/tapi")
    suspend fun getBalance(
//        @Field("nonce")
//        nonce: String,
//        @Field("method")
//        method: String = "get_info2"
    ): ZaifBalanceResponse
}