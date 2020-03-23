package jp.maskedronin.bitwatcher.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.maskedronin.bitwatcher.presentation.exchangeaccountedit.ExchangeAccountEditFragment
import jp.maskedronin.bitwatcher.presentation.exchangeaccountregister.ExchangeAccountRegisterFragment
import jp.maskedronin.bitwatcher.presentation.launch.LaunchFragment
import jp.maskedronin.bitwatcher.presentation.notification.NotificationFragment
import jp.maskedronin.bitwatcher.presentation.onboarding.OnboardingFragment
import jp.maskedronin.bitwatcher.presentation.portfolio.PortfolioFragment
import jp.maskedronin.bitwatcher.presentation.propertyregister.PropertyRegisterFragment
import jp.maskedronin.bitwatcher.presentation.settings.ExchangeAccountSettingsFragment
import jp.maskedronin.bitwatcher.presentation.settings.SettingsFragment

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeLaunchFragment(): LaunchFragment

    @ContributesAndroidInjector
    abstract fun contributeOnboardingFragment(): OnboardingFragment

    @ContributesAndroidInjector
    abstract fun contributePortfolioFragment(): PortfolioFragment

    @ContributesAndroidInjector
    abstract fun contributeNotificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeExchangeAccountRegisterFragment(): ExchangeAccountRegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeBitFlyerAccountEditFragment(): ExchangeAccountEditFragment

    @ContributesAndroidInjector
    abstract fun contributePropertyRegisterFragment(): PropertyRegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeExchangeAccountSettingsFragment(): ExchangeAccountSettingsFragment
}