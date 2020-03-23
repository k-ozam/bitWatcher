package jp.maskedronin.bitwatcher.data.api.binance

import jp.maskedronin.bitwatcher.data.api.ApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.extension.queryEncoded
import jp.maskedronin.bitwatcher.common.util.CryptUtil
import okhttp3.HttpUrl
import okhttp3.Request

class BinanceApiAuthInterceptor(
    private val apiKey: String,
    private val apiSecret: String
) : ApiAuthInterceptor() {
    override fun getAuthenticatedRequest(request: Request): Request {
        val timestamp: String = System.currentTimeMillis().toString()

        val signature: String = CryptUtil.encrypt(
            text = setOf(
                "timestamp=$timestamp",
                request.body?.queryEncoded()
            ).filterNotNull()
                .joinToString(separator = "&"),
            key = apiSecret,
            algorithm = CryptUtil.Algorithm.HMAC_SHA256
        )

        val url: HttpUrl = request.url
            .newBuilder()
            .addQueryParameter("timestamp", timestamp)
            .addQueryParameter("signature", signature)
            .build()

        return request.newBuilder()
            .url(url)
            .header(name = "X-MBX-APIKEY", value = apiKey)
            .build()
    }
}

