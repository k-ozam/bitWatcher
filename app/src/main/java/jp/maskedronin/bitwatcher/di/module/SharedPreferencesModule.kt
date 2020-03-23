package jp.maskedronin.bitwatcher.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import jp.maskedronin.bitwatcher.di.Encrypt

@Module
object SharedPreferencesModule {
    @JvmStatic
    @Provides
    fun providesSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("bitWatcher", Context.MODE_PRIVATE)

    @Encrypt
    @JvmStatic
    @Provides
    fun providesEncryptSharedPreferences(context: Context): SharedPreferences =
        EncryptedSharedPreferences.create(
            "bitWatcher-encrypted",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
}