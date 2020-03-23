package jp.maskedronin.bitwatcher.presentation.common

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import jp.maskedronin.bitwatcher.common.util.Logger

object LoggerFragmentLifecycleCallbacks : FragmentManager.FragmentLifecycleCallbacks() {
    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        Logger.i("Fragment Lifecycle onFragmentStarted: ${f::class.java.simpleName}")
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        Logger.i("Fragment Lifecycle onFragmentViewCreated: ${f::class.java.simpleName}")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        Logger.i("Fragment Lifecycle onFragmentStopped: ${f::class.java.simpleName}")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Logger.i("Fragment Lifecycle onFragmentCreated: ${f::class.java.simpleName}")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        Logger.i("Fragment Lifecycle onFragmentResumed: ${f::class.java.simpleName}")
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Logger.i("Fragment Lifecycle onFragmentAttached: ${f::class.java.simpleName}")
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Logger.i("Fragment Lifecycle onFragmentPreAttached: ${f::class.java.simpleName}")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        Logger.i("Fragment Lifecycle onFragmentDestroyed: ${f::class.java.simpleName}")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        Logger.i("Fragment Lifecycle onFragmentSaveInstanceState: ${f::class.java.simpleName}")
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        Logger.i("Fragment Lifecycle onFragmentViewDestroyed: ${f::class.java.simpleName}")
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        Logger.i("Fragment Lifecycle onFragmentPreCreated: ${f::class.java.simpleName}")
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        Logger.i("Fragment Lifecycle onFragmentActivityCreated: ${f::class.java.simpleName}")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        Logger.i("Fragment Lifecycle onFragmentPaused: ${f::class.java.simpleName}")
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        Logger.i("Fragment Lifecycle onFragmentDetached: ${f::class.java.simpleName}")
    }
}
