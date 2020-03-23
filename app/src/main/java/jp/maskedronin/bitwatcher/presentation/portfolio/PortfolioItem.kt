package jp.maskedronin.bitwatcher.presentation.portfolio

import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import org.threeten.bp.LocalDateTime

data class PortfolioItem(
    val currency: Currency,
    val settlement: Currency,
    val exchange: Exchange,
    val amount: Double,
    val rate: Double?,
    val amountUpdatedAt: LocalDateTime,
    val rateUpdatedAt: LocalDateTime?,
    val isLatestRate: Boolean
) {
    init {
        if (rate == null || rateUpdatedAt == null) {
            require(rate == null)
            require(rateUpdatedAt == null)
        }
    }

    val valuation: Double? get() = rate?.run { amount * rate }
}