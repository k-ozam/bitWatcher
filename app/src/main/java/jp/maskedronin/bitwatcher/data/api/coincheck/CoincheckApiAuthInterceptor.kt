package jp.maskedronin.bitwatcher.data.api.coincheck

import jp.maskedronin.bitwatcher.data.api.ApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.extension.queryEncoded
import jp.maskedronin.bitwatcher.common.util.CryptUtil
import okhttp3.Request

class CoincheckApiAuthInterceptor(
    private val apiKey: String,
    private val apiSecret: String
) : ApiAuthInterceptor() {
    override fun getAuthenticatedRequest(request: Request): Request {
        if (request.body?.contentLength() ?: 0 > 0) {
            throw UnsupportedOperationException()
        }

        val timestamp: String = System.currentTimeMillis().div(1000).toString()

        return request.newBuilder()
            .header(name = "ACCESS-KEY", value = apiKey)
            .header(name = "ACCESS-NONCE", value = timestamp)
            .header(
                name = "ACCESS-SIGNATURE",
                value = CryptUtil.encrypt(
                    text = timestamp + request.url.toString() +
                            (request.body?.queryEncoded() ?: ""),
                    key = apiSecret,
                    algorithm = CryptUtil.Algorithm.HMAC_SHA256
                )
            ).build()
    }
}