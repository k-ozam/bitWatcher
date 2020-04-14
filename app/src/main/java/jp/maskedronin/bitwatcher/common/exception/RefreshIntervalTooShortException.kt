package jp.maskedronin.bitwatcher.common.exception

class RateRefreshIntervalTooShortException(
    override val interval: Long
) : RefreshIntervalTooShortException()

class AmountRefreshIntervalTooShortException(
    override val interval: Long
) : RefreshIntervalTooShortException()

abstract class RefreshIntervalTooShortException : RuntimeException() {
    abstract val interval: Long
}