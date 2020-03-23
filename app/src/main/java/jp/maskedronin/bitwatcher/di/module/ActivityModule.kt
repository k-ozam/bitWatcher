package jp.maskedronin.bitwatcher.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.maskedronin.bitwatcher.presentation.MainActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}