package jp.maskedronin.bitwatcher.data.source

import jp.maskedronin.bitwatcher.data.api.binance.BinanceApi
import jp.maskedronin.bitwatcher.data.api.binancejex.BinanceJexApi
import jp.maskedronin.bitwatcher.data.api.bitflyer.BitFlyerApi
import jp.maskedronin.bitwatcher.data.api.coincheck.CoincheckApi
import jp.maskedronin.bitwatcher.data.api.zaif.ZaifApi
import jp.maskedronin.bitwatcher.domain.entity.Property
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class PropertyRemoteDataSource @Inject constructor(
    private val bitFlyerApi: BitFlyerApi,
    private val coincheckApi: CoincheckApi,
    private val zaifApi: ZaifApi,
    private val binanceApi: BinanceApi,
    private val binanceJexApi: BinanceJexApi
) {
    private suspend fun getCurrencyToAmount(exchange: Exchange): Map<Currency, Double> =
        when (exchange) {
            Exchange.BITFLYER -> bitFlyerApi.getBalanceList().associate { balance ->
                Currency.findBySymbol(balance.currencyCode) to balance.amount
            }

            Exchange.COINCHECK -> coincheckApi.getBalance().let { balance ->
                mapOf(
                    Currency.BTC to balance.btc,
                    Currency.JPY to balance.jpy,
                    Currency.BCH to balance.bch,
                    Currency.ETC to balance.etc,
                    Currency.ETH to balance.eth,
                    Currency.FCT to balance.fct,
                    Currency.LSK to balance.lsk,
                    Currency.LTC to balance.ltc,
                    Currency.MONA to balance.mona,
                    Currency.QTUM to balance.qtum,
                    Currency.XEM to balance.xem,
                    Currency.XRP to balance.xrp
                )
            }

            Exchange.ZAIF -> zaifApi.getBalance().zaifBalanceWrapper.funds.mapKeys {
                Currency.findBySymbol(it.key)
            }

            Exchange.BINANCE -> binanceApi.getUserInfo().balances
                .associate {
                    Currency.findBySymbol(it.asset) to it.free
                }

            Exchange.BINANCE_JEX -> binanceJexApi.getAccount().spotBalances.associate {
                Currency.findBySymbol(it.asset) to it.free
            }

            else -> TODO()
        }.filterKeys { it != null }
            .mapKeys { it.key as Currency }

    suspend fun getProperties(exchange: Exchange): List<Property> = getCurrencyToAmount(exchange)
        .filter {
            val amount = it.value
            amount > 0
        }.map {
            val (currency, amount) = it
            Property(
                currency,
                amount,
                exchange,
                updatedAt = LocalDateTime.now()
            )
        }
}

