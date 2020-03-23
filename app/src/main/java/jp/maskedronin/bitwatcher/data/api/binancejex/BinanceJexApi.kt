package jp.maskedronin.bitwatcher.data.api.binancejex

import jp.maskedronin.bitwatcher.data.api.RequireAuth
import retrofit2.http.GET

/**
 * https://github.com/JexApi/jex-official-api-docs
 */
interface BinanceJexApi {
    companion object {
        const val ENDPOINT = "https://www.jex.com/"
    }

    @Deprecated("invoke through BinanceJexTickerApiWrapper")
    @GET("/api/v2/pub/jexcap")
    suspend fun getTicker(): Map<String, BinanceJexTicker>

    @RequireAuth
    @GET("/api/v1/account")
    suspend fun getAccount(): BinanceJexAccount
}