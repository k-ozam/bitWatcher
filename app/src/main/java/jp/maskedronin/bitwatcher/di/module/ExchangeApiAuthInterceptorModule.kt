package jp.maskedronin.bitwatcher.di.module

import dagger.Module
import dagger.Provides
import jp.maskedronin.bitwatcher.data.api.binance.BinanceApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.api.binancejex.BinanceJexApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.api.bitflyer.BitFlyerApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.api.coincheck.CoincheckApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.api.zaif.ZaifApiAuthInterceptor
import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.repository.ExchangeAccountRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.runBlocking

@Module
object ExchangeApiAuthInterceptorModule {
    @JvmStatic
    @Provides
    fun providesBitFlyerApiAuthInterceptor(
        exchangeAccountRepository: ExchangeAccountRepository
    ): BitFlyerApiAuthInterceptor? {
        val account: ExchangeAccount = runBlocking {
            exchangeAccountRepository.getAccount(exchange = Exchange.BITFLYER)
        } ?: return null

        return BitFlyerApiAuthInterceptor(account.apiKey, account.apiSecret)
    }

    @JvmStatic
    @Provides
    fun providesCoincheckApiAuthInterceptor(
        exchangeAccountRepository: ExchangeAccountRepository
    ): CoincheckApiAuthInterceptor? {
        val account: ExchangeAccount = runBlocking {
            exchangeAccountRepository.getAccount(exchange = Exchange.COINCHECK)
        } ?: return null

        return CoincheckApiAuthInterceptor(account.apiKey, account.apiSecret)
    }

    @JvmStatic
    @Provides
    fun providesZaifApiAuthInterceptor(
        exchangeAccountRepository: ExchangeAccountRepository
    ): ZaifApiAuthInterceptor? {
        val account: ExchangeAccount = runBlocking {
            exchangeAccountRepository.getAccount(exchange = Exchange.ZAIF)
        } ?: return null

        return ZaifApiAuthInterceptor(account.apiKey, account.apiSecret)
    }

    @JvmStatic
    @Provides
    fun providesBinanceApiAuthInterceptor(
        exchangeAccountRepository: ExchangeAccountRepository
    ): BinanceApiAuthInterceptor? {
        val account: ExchangeAccount = runBlocking {
            exchangeAccountRepository.getAccount(exchange = Exchange.BINANCE)
        } ?: return null

        return BinanceApiAuthInterceptor(account.apiKey, account.apiSecret)
    }

    @JvmStatic
    @Provides
    fun providesBinanceJexApiAuthInterceptor(
        exchangeAccountRepository: ExchangeAccountRepository
    ): BinanceJexApiAuthInterceptor? {
        val account: ExchangeAccount = runBlocking {
            exchangeAccountRepository.getAccount(exchange = Exchange.BINANCE_JEX)
        } ?: return null

        return BinanceJexApiAuthInterceptor(account.apiKey, account.apiSecret)
    }
}