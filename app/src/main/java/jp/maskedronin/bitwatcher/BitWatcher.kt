package jp.maskedronin.bitwatcher

import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import jp.maskedronin.bitwatcher.di.DaggerAppComponent
import jp.maskedronin.bitwatcher.common.util.Logger
import jp.maskedronin.bitwatcher.presentation.RestartActivity

class BitWatcher : DaggerApplication() {
    companion object {
        fun restart(context: Context) {
            RestartActivity.start(context)
        }
    }

    override fun onCreate() {
        super.onCreate()

        Logger.initialize()
        AndroidThreeTen.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)
}