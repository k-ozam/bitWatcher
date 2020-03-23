package jp.maskedronin.bitwatcher.di.module

import dagger.Module
import dagger.Provides
import jp.maskedronin.bitwatcher.data.api.binance.BinanceApi
import jp.maskedronin.bitwatcher.data.api.binance.BinanceApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.api.binancejex.BinanceJexApi
import jp.maskedronin.bitwatcher.data.api.binancejex.BinanceJexApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.api.bitbank.BitbankApi
import jp.maskedronin.bitwatcher.data.api.bitflyer.BitFlyerApi
import jp.maskedronin.bitwatcher.data.api.bitflyer.BitFlyerApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.api.bittrex.BittrexApi
import jp.maskedronin.bitwatcher.data.api.coincheck.CoincheckApi
import jp.maskedronin.bitwatcher.data.api.coincheck.CoincheckApiAuthInterceptor
import jp.maskedronin.bitwatcher.data.api.cryptowatch.CryptowatchApi
import jp.maskedronin.bitwatcher.data.api.gmocoin.GmoCoinApi
import jp.maskedronin.bitwatcher.data.api.poloniex.PoloniexApi
import jp.maskedronin.bitwatcher.data.api.zaif.ZaifApi
import jp.maskedronin.bitwatcher.data.api.zaif.ZaifApiAuthInterceptor
import jp.maskedronin.bitwatcher.di.ClientBuiltin
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [ExchangeApiAuthInterceptorModule::class])
object ApiModule {
    @Singleton
    @JvmStatic
    @Provides
    fun providesBitFlyerApi(
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder,
        authInterceptor: BitFlyerApiAuthInterceptor?
    ): BitFlyerApi {
        authInterceptor?.run {
            okHttpClientBuilder.addInterceptor(authInterceptor)
        }

        return retrofitBuilder
            .client(okHttpClientBuilder.build())
            .baseUrl(BitFlyerApi.ENDPOINT)
            .build()
            .create(BitFlyerApi::class.java)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesCoincheckApi(
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder,
        authInterceptor: CoincheckApiAuthInterceptor?
    ): CoincheckApi {
        authInterceptor?.run {
            okHttpClientBuilder.addInterceptor(authInterceptor)
        }

        return retrofitBuilder
            .client(okHttpClientBuilder.build())
            .baseUrl(CoincheckApi.ENDPOINT)
            .build()
            .create(CoincheckApi::class.java)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesZaifApi(
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder,
        authInterceptor: ZaifApiAuthInterceptor?
    ): ZaifApi {
        authInterceptor?.run {
            okHttpClientBuilder.addInterceptor(authInterceptor)
        }

        return retrofitBuilder
            .client(okHttpClientBuilder.build())
            .baseUrl(ZaifApi.ENDPOINT)
            .build()
            .create(ZaifApi::class.java)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesGmoCoinApi(
        @ClientBuiltin
        retrofitBuilder: Retrofit.Builder
    ): GmoCoinApi = retrofitBuilder
        .baseUrl(GmoCoinApi.ENDPOINT)
        .build()
        .create(GmoCoinApi::class.java)

    @Singleton
    @JvmStatic
    @Provides
    fun providesBitbankApi(
        @ClientBuiltin
        retrofitBuilder: Retrofit.Builder
    ): BitbankApi = retrofitBuilder
        .baseUrl(BitbankApi.ENDPOINT)
        .build()
        .create(BitbankApi::class.java)

    @Singleton
    @JvmStatic
    @Provides
    fun providesCryptowatchApi(
        @ClientBuiltin
        retrofitBuilder: Retrofit.Builder
    ): CryptowatchApi = retrofitBuilder
        .baseUrl(CryptowatchApi.ENDPOINT)
        .build()
        .create(CryptowatchApi::class.java)

    @Singleton
    @JvmStatic
    @Provides
    fun providesBittrexApi(
        @ClientBuiltin
        retrofitBuilder: Retrofit.Builder
    ): BittrexApi = retrofitBuilder
        .baseUrl(BittrexApi.ENDPOINT)
        .build()
        .create(BittrexApi::class.java)

    @Singleton
    @JvmStatic
    @Provides
    fun providesBinanceApi(
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder,
        authInterceptor: BinanceApiAuthInterceptor?
    ): BinanceApi {
        authInterceptor?.run {
            okHttpClientBuilder.addInterceptor(authInterceptor)
        }

        return retrofitBuilder
            .client(okHttpClientBuilder.build())
            .baseUrl(BinanceApi.ENDPOINT)
            .build()
            .create(BinanceApi::class.java)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesBinanceJexApi(
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder,
        authInterceptor: BinanceJexApiAuthInterceptor?
    ): BinanceJexApi {
        authInterceptor?.run {
            okHttpClientBuilder.addInterceptor(authInterceptor)
        }

        return retrofitBuilder
            .client(okHttpClientBuilder.build())
            .baseUrl(BinanceJexApi.ENDPOINT)
            .build()
            .create(BinanceJexApi::class.java)
    }

    @Singleton
    @JvmStatic
    @Provides
    fun providesPoloniexApi(
        @ClientBuiltin
        retrofitBuilder: Retrofit.Builder
    ): PoloniexApi = retrofitBuilder
        .baseUrl(PoloniexApi.ENDPOINT)
        .build()
        .create(PoloniexApi::class.java)
}