package jp.maskedronin.bitwatcher.data.extension

import okhttp3.FormBody
import okhttp3.RequestBody

fun RequestBody.queryEncoded(): String? = toMap()
    .map { "${it.key}=${it.value}" }
    .joinToString(separator = "&")
    .let { if (it.isEmpty()) null else it }

fun RequestBody.toMap(): Map<String, String> {
    require(this is FormBody)

    return (0 until size).associate { i -> name(i) to value(i) }
}
