package jp.maskedronin.bitwatcher.presentation.common

import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.common.exception.ExchangeApiUnauthorizedException
import jp.maskedronin.bitwatcher.common.exception.NetworkInactiveException
import jp.maskedronin.bitwatcher.common.util.Logger
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import retrofit2.HttpException
import java.net.UnknownHostException

class ThrowableHandler(
    private val onHandle: (StringResource) -> Unit
) {
    fun handle(t: Throwable) {
        Logger.e(t)

        val message: StringResource = resolveMessage(t) ?: throw t
        onHandle(message)
    }

    private fun resolveMessage(t: Throwable): StringResource? = when (t) {
        is ExchangeApiUnauthorizedException -> StringResource.from(
            R.string.error_exchange_api_unauthorized_message,
            t.exchange.canonicalName
        )
        is NetworkInactiveException ->
            StringResource.from(R.string.error_network_inactive_message)
        is HttpException -> t.toUserNotifiableMessage()
        is RuntimeException -> StringResource.from(R.string.error_unknown)
        is UnknownHostException -> StringResource.from(R.string.error_unknown_host)
        else -> null
    }
}

private fun HttpException.toUserNotifiableMessage(): StringResource = when (code()) {
    400 -> R.string.error_http_request_invalid_message
    401 -> R.string.error_http_unauthorized_message
    in 402..499 -> R.string.error_http_unknown_message
    in 500..599 -> R.string.error_http_internal_server_message
    else -> R.string.error_http_unknown_message
}.let { StringResource.from(it) }
