package jp.maskedronin.bitwatcher.di.module

import dagger.Binds
import dagger.Module
import jp.maskedronin.bitwatcher.data.repository.ExchangeAccountRepositoryImpl
import jp.maskedronin.bitwatcher.data.repository.ExchangeRateRepositoryImpl
import jp.maskedronin.bitwatcher.data.repository.PropertyRepositoryImpl
import jp.maskedronin.bitwatcher.data.repository.SettingsRepositoryImpl
import jp.maskedronin.bitwatcher.domain.repository.ExchangeAccountRepository
import jp.maskedronin.bitwatcher.domain.repository.ExchangeRateRepository
import jp.maskedronin.bitwatcher.domain.repository.PropertyRepository
import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindsExchangeRateRepository(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    @Binds
    abstract fun bindsPropertyRepository(impl: PropertyRepositoryImpl): PropertyRepository

    @Binds
    abstract fun bindsExchangeAccountRepository(impl: ExchangeAccountRepositoryImpl): ExchangeAccountRepository

    @Binds
    abstract fun bindsSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}