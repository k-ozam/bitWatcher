package jp.maskedronin.bitwatcher.data.api.binancejex

import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.common.util.Constants
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BinanceJexTickerApiWrapper @Inject constructor(
    private val binanceJexApi: BinanceJexApi
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
        ticker = binanceJexApi.getTicker()
            .filterKeys { it.contains("/") }
            .mapKeys { map ->
                val (baseSymbol, quoteSymbol) = map.key.split("/")
                    .let { it[0] to it[1] }
                Currency.findBySymbol(baseSymbol) to Currency.findBySymbol(quoteSymbol)
            }
            .filterKeys {
                it.first != null && it.second != null
            }
            .mapKeys { it.key as Pair<Currency, Currency> }
            .mapValues { it.value.highestBid }

        lastRefreshedAt = LocalDateTime.now()
    }
}