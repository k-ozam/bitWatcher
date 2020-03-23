package jp.maskedronin.bitwatcher.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import jp.maskedronin.bitwatcher.BuildConfig
import jp.maskedronin.bitwatcher.common.extension.nextCharArray
import jp.maskedronin.bitwatcher.data.db.AppDatabase
import jp.maskedronin.bitwatcher.data.db.dao.ExchangeAccountDao
import jp.maskedronin.bitwatcher.data.db.dao.ExchangeRateDao
import jp.maskedronin.bitwatcher.data.db.dao.PropertyDao
import jp.maskedronin.bitwatcher.data.sharedpreferences.SharedPreferencesWrapper
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.security.SecureRandom
import javax.inject.Singleton

private typealias SqlcipherFactory = SupportFactory

@Module
object DatabaseModule {
    @Singleton
    @JvmStatic
    @Provides
    fun providesDatabase(
        context: Context,
        sharedPreferencesWrapper: SharedPreferencesWrapper
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            BuildConfig.DB_NAME
        ).also { builder ->
            val passphrase = sharedPreferencesWrapper.getDatabasePassphrase()
                ?: run {
                    val passphrase: String = if (BuildConfig.DEBUG) {
                        BuildConfig.DB_PASSPHRASE
                    } else {
                        val chars = CharArray(1000)
                        SecureRandom().nextCharArray(chars)
                        String(chars)
                    }
                    sharedPreferencesWrapper.setDatabasePassphrase(passphrase)
                    passphrase
                }
            val passphraseBytes = SQLiteDatabase.getBytes(
                passphrase.toCharArray()
            )

            builder.openHelperFactory(
                SqlcipherFactory(passphraseBytes)
            )
        }.build()

    @JvmStatic
    @Provides
    fun providesPropertyDao(appDatabase: AppDatabase): PropertyDao = appDatabase.propertyDao()

    @JvmStatic
    @Provides
    fun providesExchangeRateDao(appDatabase: AppDatabase): ExchangeRateDao =
        appDatabase.exchangeRateDao()

    @JvmStatic
    @Provides
    fun providesExchangeApiAuthStatusDao(appDatabase: AppDatabase): ExchangeAccountDao =
        appDatabase.exchangeAccountDao()
}