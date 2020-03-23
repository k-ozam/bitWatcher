package jp.maskedronin.bitwatcher.di.module

import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import jp.maskedronin.bitwatcher.BuildConfig
import jp.maskedronin.bitwatcher.di.ClientBuiltin
import jp.maskedronin.bitwatcher.common.exception.NetworkInactiveException
import jp.maskedronin.bitwatcher.common.util.isConnected
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
object NetworkModule {
    @JvmStatic
    @Provides
    fun providesOkHttpClientBuilder(
        connectivityManager: ConnectivityManager
    ): OkHttpClient.Builder = OkHttpClient.Builder()
        .eventListener(object : EventListener() {
            override fun callStart(call: Call) {
                if (connectivityManager.isConnected().not()) {
                    throw NetworkInactiveException
                }
            }
        })
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
        }

    @JvmStatic
    @Provides
    @kotlinx.serialization.UnstableDefault
    fun providesRetrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(
            Json(
                JsonConfiguration(
                    ignoreUnknownKeys = true,
                    isLenient = true
                )
            ).asConverterFactory("application/json".toMediaType())
        )

    @ClientBuiltin
    @JvmStatic
    @Provides
    fun providesClientBuiltinRetrofitBuilder(
        okHttpClientBuilder: OkHttpClient.Builder,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit.Builder = retrofitBuilder
        .client(okHttpClientBuilder.build())

    @JvmStatic
    @Provides
    fun providesConnectivityManager(
        context: Context
    ): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager
}