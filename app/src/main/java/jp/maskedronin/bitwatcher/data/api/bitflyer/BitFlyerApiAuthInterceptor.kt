package jp.maskedronin.bitwatcher.data.api.bitflyer

import jp.maskedronin.bitwatcher.common.util.CryptUtil
import jp.maskedronin.bitwatcher.data.api.ApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.extension.queryEncoded
import okhttp3.Request

class BitFlyerApiAuthInterceptor(
    private val apiKey: String,
    private val apiSecret: String
) : ApiAuthInterceptor() {
    override fun getAuthenticatedRequest(request: Request): Request {
        val timestamp: String = System.currentTimeMillis().div(1000).toString()
        return request.newBuilder()
            .header(name = "ACCESS-KEY", value = apiKey)
            .header(name = "ACCESS-TIMESTAMP", value = timestamp)
            .header(
                name = "ACCESS-SIGN",
                value = CryptUtil.encrypt(
                    text = timestamp + request.method + request.url.encodedPath +
                            (request.body?.queryEncoded() ?: ""),
                    key = apiSecret,
                    algorithm = CryptUtil.Algorithm.HMAC_SHA256
                )
            ).build()
    }
}

