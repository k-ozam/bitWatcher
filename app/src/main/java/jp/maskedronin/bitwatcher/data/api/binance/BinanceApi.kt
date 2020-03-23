package jp.maskedronin.bitwatcher.data.api.binance

import jp.maskedronin.bitwatcher.data.api.RequireAuth
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApi {
    companion object {
        const val ENDPOINT = "https://api.binance.com/"
    }

    @GET("/api/v3/ticker/price")
    suspend fun getTicker(@Query("symbol") symbol: String): BinanceTicker

    @RequireAuth
    @GET("/api/v3/account")
    suspend fun getUserInfo(): BinanceUserInfo
}