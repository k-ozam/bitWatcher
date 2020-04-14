package jp.maskedronin.bitwatcher.data.api.poloniex

import jp.maskedronin.bitwatcher.common.util.Constants
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PoloniexTickerApiWrapper @Inject constructor(
    private val poloniexApi: PoloniexApi
) {
    private var ticker: Map<Pair<Currency, Currency>, Double>? = null
    private var lastRefreshedAt: LocalDateTime? = null

    suspend fun getBid(base: Currency, quote: Currency): Double {
        if (lastRefreshedAt
                ?.plusSeconds(Constants.TICKER_API_LARGE_RESPONSE_CACHE_SECONDS)
                ?.isBefore(LocalDateTime.now()) != false
        ) {
            refreshTicker()
        }

        return ticker!!.getValue(base to quote)
    }

    private suspend fun refreshTicker() {
        ticker = poloniexApi.getTicker()
            .mapKeys { map ->
                map.key.split("_").let {
                    Currency.findBySymbol(it[0]) to Currency.findBySymbol(it[1])
                }
            }.filterKeys { it.first != null && it.second != null }
            .mapKeys { it.key as Pair<Currency, Currency> }
            .mapValues { it.value.highestBid }

        lastRefreshedAt = LocalDateTime.now()
    }
}