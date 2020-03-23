package jp.maskedronin.bitwatcher.common.util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

object CryptUtil {
    enum class Algorithm(val algorithmName: String) {
        HMAC_SHA256("HmacSHA256"),
        HMAC_SHA512("HmacSHA512")
    }

    fun encrypt(text: String, key: String, algorithm: Algorithm): String =
        Mac.getInstance(algorithm.algorithmName)
            .also { mac ->
                mac.init(SecretKeySpec(key.toByteArray(), mac.algorithm))
            }
            .doFinal(text.toByteArray())
            .joinToString("") {
                String.format("%02x", it and 255.toByte())
            }
}