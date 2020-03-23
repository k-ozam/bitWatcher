package jp.maskedronin.bitwatcher.data.api.coincheck

import androidx.annotation.StringDef
import jp.maskedronin.bitwatcher.data.api.RequireAuth
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoincheckApi {
    companion object {
        const val ENDPOINT = "https://coincheck.com/"

        @Retention(AnnotationRetention.SOURCE)
        @StringDef(
            ORDER_TYPE_SELL,
            ORDER_TYPE_BUY
        )
        annotation class OrderType

        const val ORDER_TYPE_SELL = "sell"
        const val ORDER_TYPE_BUY = "buy"
    }

    /**
     * @param pair 取引ペア。現在は "btc_jpy", "fct_jpy" のみ。
     * @param amount と
     * @param price はどちらか一方だけ指定する。
     */
    @GET("/api/exchange/orders/rate")
    suspend fun getRate(
        @Query("order_type")
        @OrderType
        orderType: String,
        @Query("pair")
        pair: String,
        @Query("amount")
        amount: Double? = null,
        @Query("price")
        price: Double? = null
    ): CoincheckRate

    @GET("/api/rate/{pair}")
    suspend fun getBuyRate(
        @Path("pair")
        pair: String
    ): CoincheckBuyRate

    @RequireAuth
    @GET("/api/accounts/balance")
    suspend fun getBalance(): CoincheckBalance
}