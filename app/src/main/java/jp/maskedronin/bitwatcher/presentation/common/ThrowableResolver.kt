package jp.maskedronin.bitwatcher.presentation.common

import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.common.exception.ExchangeApiUnauthorizedException
import jp.maskedronin.bitwatcher.common.exception.NetworkInactiveException
import jp.maskedronin.bitwatcher.common.util.Logger
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * @param onHandle (message, messageType)
 */
class ThrowableHandler(
    private val onHandle: (StringResource, MessageType) -> Unit
) {
    enum class MessageType {
        SHORT_SENTENCE,
        LONG_SENTENCE,
    }

    fun handle(t: Throwable) {
        Logger.e(t)

        val (message: StringResource, type: MessageType) = resolveMessage(t) ?: throw t
        onHandle(message, type)
    }

    private fun resolveMessage(t: Throwable): Pair<StringResource, MessageType>? = when (t) {
        is ExchangeApiUnauthorizedException -> StringResource.from(
            R.string.error_exchange_api_unauthorized_message,
            t.exchange.canonicalName
        ) to MessageType.LONG_SENTENCE
        is NetworkInactiveException ->
            StringResource.from(R.string.error_network_inactive_message) to MessageType.SHORT_SENTENCE
        is HttpException -> t.toUserNotifiableMessage() to MessageType.LONG_SENTENCE
        is RuntimeException -> StringResource.from(R.string.error_unknown) to MessageType.SHORT_SENTENCE
        is UnknownHostException -> StringResource.from(R.string.error_unknown_host) to MessageType.SHORT_SENTENCE
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
