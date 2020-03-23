package jp.maskedronin.bitwatcher.data.api.cryptowatch

import retrofit2.http.GET

interface CryptowatchApi {
    companion object {
        const val ENDPOINT = "https://api.cryptowat.ch/"
    }

    @GET("pairs/btcjpy")
    suspend fun getPair(): CryptowatchPairWrapper
}