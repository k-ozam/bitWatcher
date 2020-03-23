package jp.maskedronin.bitwatcher.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import jp.maskedronin.bitwatcher.BitWatcher
import jp.maskedronin.bitwatcher.di.module.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        NetworkModule::class,
        ApiModule::class,
        DatabaseModule::class,
        SharedPreferencesModule::class,
        RepositoryModule::class,
        ActivityModule::class,
        FragmentModule::class
    ]
)
interface AppComponent : AndroidInjector<BitWatcher> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}