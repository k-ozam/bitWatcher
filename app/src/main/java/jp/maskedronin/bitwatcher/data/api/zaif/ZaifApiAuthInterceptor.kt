package jp.maskedronin.bitwatcher.data.api.zaif

import jp.maskedronin.bitwatcher.common.util.CryptUtil
import jp.maskedronin.bitwatcher.data.api.ApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.extension.getAnnotation
import jp.maskedronin.bitwatcher.data.extension.queryEncoded
import okhttp3.FormBody
import okhttp3.Request

class ZaifApiAuthInterceptor(
    private val apiKey: String,
    private val apiSecret: String
) : ApiAuthInterceptor() {
    override fun getAuthenticatedRequest(request: Request): Request {
        if (request.body?.contentLength() ?: 0 > 0) {
            throw UnsupportedOperationException()
        }

        val requestBody: FormBody = FormBody.Builder()
            .add(
                name = "nonce",
                value = System.currentTimeMillis().div(1000).toString()
            )
            .add(
                name = "method",
                value = request.getAnnotation(ZaifApiMethod::class)!!.value
            )
            .build()

        return request.newBuilder()
            .post(requestBody)
            .header(name = "key", value = apiKey)
            .header(
                name = "sign",
                value = CryptUtil.encrypt(
                    text = requestBody.queryEncoded() ?: "",
                    key = apiSecret,
                    algorithm = CryptUtil.Algorithm.HMAC_SHA512
                )
            ).build()
    }
}
