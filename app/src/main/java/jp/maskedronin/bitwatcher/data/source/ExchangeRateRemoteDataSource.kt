package jp.maskedronin.bitwatcher.data.source

import jp.maskedronin.bitwatcher.data.api.binance.BinanceApi
import jp.maskedronin.bitwatcher.data.api.binancejex.BinanceJexTickerApiWrapper
import jp.maskedronin.bitwatcher.data.api.bitbank.BitbankApi
import jp.maskedronin.bitwatcher.data.api.bitflyer.BitFlyerApi
import jp.maskedronin.bitwatcher.data.api.bittrex.BittrexApi
import jp.maskedronin.bitwatcher.data.api.coincheck.CoincheckApi
import jp.maskedronin.bitwatcher.data.api.cryptowatch.CryptowatchApi
import jp.maskedronin.bitwatcher.data.api.gmocoin.GmoCoinApi
import jp.maskedronin.bitwatcher.data.api.poloniex.PoloniexTickerApiWrapper
import jp.maskedronin.bitwatcher.data.api.zaif.ZaifApi
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRemoteDataSource @Inject constructor(
    private val bitFlyerApi: BitFlyerApi,
    private val coincheckApi: CoincheckApi,
    private val zaifApi: ZaifApi,
    private val bittrexApi: BittrexApi,
    private val binanceApi: BinanceApi,
    private val binanceJexTickerApi: BinanceJexTickerApiWrapper,
    private val poloniexTickerApi: PoloniexTickerApiWrapper,
    private val gmoCoinApi: GmoCoinApi,
    private val bitbankApi: BitbankApi,
    private val cryptowatchApi: CryptowatchApi
) {
    suspend fun getRate(
        base: Currency,
        quote: Currency,
        exchange: Exchange
    ): Double {
        if (exchange.isSupportingPairReversedAlso(pair = base to quote)) {
            return getRateInternal(base, quote, exchange)
        }

        Exchange.values()
            .filterNot { it == exchange }
            .find { it.isSupportingPairReversedAlso(base to quote) }
            ?.also { return getRateInternal(base, quote, it) }

        // base/BTC と BTC/quote を取得して base/quote を概算する
        val relayCurrencyAndExchange =
            getRelayCurrencyAndExchanges(
                base,
                quote
            )
        val firstHalfRate = getRateInternal(
            base = base,
            quote = relayCurrencyAndExchange.relayCurrency,
            exchange = relayCurrencyAndExchange.firstRelayExchange
        )
        val secondHalfRate = getRateInternal(
            base = relayCurrencyAndExchange.relayCurrency,
            quote = quote,
            exchange = relayCurrencyAndExchange.secondRelayExchange
        )
        return firstHalfRate * secondHalfRate
    }

    /**
     * base/quote, quote/base both supported
     */
    private suspend fun getRateInternal(
        base: Currency,
        quote: Currency,
        exchange: Exchange
    ): Double {
        val isPairSupported: Boolean = exchange.isSupporting(base to quote)
        val isReversedPairSupported: Boolean = exchange.isSupporting(quote to base)
        val pair: Pair<Currency, Currency> = when {
            isPairSupported -> base to quote
            isReversedPairSupported -> quote to base
            else -> throw UnsupportedOperationException(
                "${base.getSymbol()}/${quote.getSymbol()}, ${quote.getSymbol()}/${base.getSymbol()} are not supported by ${exchange.canonicalName}"
            )
        }

        return when (exchange) {
            Exchange.BITFLYER ->
                bitFlyerApi.getTicker(
                    with(pair) { "${first.getSymbol()}_${second.getSymbol()}" }
                ).bestBid

            Exchange.COINCHECK ->
                when {
                    (pair == Currency.BTC to Currency.JPY) || (pair == Currency.FCT to Currency.JPY) ->
                        coincheckApi.getRate(
                            orderType = CoincheckApi.ORDER_TYPE_SELL,
                            pair = with(pair) {
                                "${first.getSymbol().toLowerCase(Locale.US)}_${
                                second.getSymbol().toLowerCase(Locale.US)}"
                            },
                            amount = 1.0
                        ).rate

                    else -> coincheckApi.getBuyRate(
                        with(pair) {
                            "${first.getSymbol().toLowerCase(Locale.US)}_${second.getSymbol()
                                .toLowerCase(Locale.US)}"
                        }
                    ).rate
                }

            Exchange.ZAIF -> zaifApi.getTicker(
                with(pair) {
                    "${first.getSymbol().toLowerCase(Locale.US)}_${second.getSymbol()
                        .toLowerCase(Locale.US)}"
                }
            ).bid

            Exchange.GMOCOIN -> gmoCoinApi.getTicker(
                with(pair) { "${first.getSymbol()}_${second.getSymbol()}" }
            ).data.first().bid

            Exchange.BITBANK -> bitbankApi.getTicker(
                with(pair) {
                    "${first.getSymbol().toLowerCase(Locale.US)}_${second.getSymbol()
                        .toLowerCase(Locale.US)}"
                }
            ).data.buy

            Exchange.BITTREX -> bittrexApi.getTicker(
                with(pair) { "${first.getSymbol()}-${second.getSymbol()}" }
            ).bidRate

            Exchange.BINANCE -> binanceApi.getTicker(
                symbol = with(pair) { "${first.getSymbol()}${second.getSymbol()}" }
            ).price

            Exchange.BINANCE_JEX -> binanceJexTickerApi.getBid(
                base = pair.first, quote = pair.second
            )

            Exchange.POLONIEX -> poloniexTickerApi.getBid(
                base = pair.first, quote = pair.second
            )
        }.let { rate ->
            when {
                isPairSupported -> rate
                isReversedPairSupported -> 1 / rate
                else -> error("unknown state")
            }
        }
    }
}

private fun Exchange.isSupportingPairReversedAlso(pair: Pair<Currency, Currency>): Boolean =
    isSupporting(pair) || isSupporting(pair.second to pair.first)

private fun getRelayCurrencyAndExchanges(
    base: Currency,
    quote: Currency
): RelayCurrencyAndExchange {
    val firstHalfRelayExchangeAndPairProposalList = Exchange.values()
        .map { _exchange ->
            _exchange to Currency.values()
                .filter { currency ->
                    _exchange.isSupportingPairReversedAlso(base to currency)
                }
        }
        .filter { it.second.isNotEmpty() }
    val secondHalfRelayExchangeAndPairProposalList = Exchange.values()
        .map { _exchange ->
            _exchange to Currency.values()
                .filter { currency ->
                    _exchange.isSupportingPairReversedAlso(currency to quote)
                }
        }
        .filter { it.second.isNotEmpty() }
    val relayCurrency: Currency =
        firstHalfRelayExchangeAndPairProposalList
            .flatMap { it.second }
            .distinct()
            .intersect(
                secondHalfRelayExchangeAndPairProposalList
                    .flatMap { it.second }
                    .distinct()
            )
            .also {
                if (it.isEmpty()) {
                    error("relay currency for ${base.getSymbol()} -> ${quote.getSymbol()} not found")
                }
            }
            .first()

    val firstHalfRelayExchangeProposalList = firstHalfRelayExchangeAndPairProposalList
        .filter { it.second.contains(relayCurrency) }
    val secondHalfRelayExchangeProposalList = secondHalfRelayExchangeAndPairProposalList
        .filter { it.second.contains(relayCurrency) }

    return RelayCurrencyAndExchange(
        relayCurrency,
        firstRelayExchange = firstHalfRelayExchangeProposalList.first().first,
        secondRelayExchange = secondHalfRelayExchangeProposalList.first().first
    )
}

private data class RelayCurrencyAndExchange(
    val relayCurrency: Currency,
    val firstRelayExchange: Exchange,
    val secondRelayExchange: Exchange
)