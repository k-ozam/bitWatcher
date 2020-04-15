package jp.maskedronin.bitwatcher.common.exception

import jp.maskedronin.bitwatcher.domain.valueobject.Exchange

class ExchangeApiUnauthorizedException(
    val exchange: Exchange,
    cause: Throwable
) : RuntimeException(
    "exchange=${exchange.canonicalName}",
    cause
)