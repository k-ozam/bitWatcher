package jp.maskedronin.bitwatcher.domain.entity

import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource

sealed class Notification {
    abstract val message: StringResource

    data class Unauthorized(
        val exchange: Exchange
    ) : Notification() {
        override val message: StringResource
            get() = StringResource.from(
                R.string.error_exchange_api_unauthorized_message,
                exchange.canonicalName
            )
    }
}