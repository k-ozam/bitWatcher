package jp.maskedronin.bitwatcher.common.util

import jp.maskedronin.bitwatcher.BuildConfig
import timber.log.Timber
import kotlin.reflect.KClass

/**
 * a wrapper of [Timber]
 */
object Logger {
    fun initialize() {
        val tree: Timber.Tree = if (BuildConfig.DEBUG) {
            DebugTree
        } else {
            ReleaseTree
        }

        Timber.plant(tree)
    }

    fun d(message: String?, vararg args: Any) {
        Timber.d(message, *args)
    }

    fun i(message: String?, vararg args: Any) {
        Timber.i(message, *args)
    }

    fun w(message: String?, vararg args: Any) {
        Timber.w(message, *args)
    }

    fun e(message: String?, vararg args: Any) {
        Timber.e(message, *args)
    }

    fun e(t: Throwable?, message: String? = null, vararg args: Any) {
        Timber.e(t, message, *args)
    }

    fun wtf(t: Throwable?, message: String?, vararg args: Any) {
        Timber.wtf(t, message, *args)
    }
}

object ReleaseTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        t?.run {
            // TODO: Crashlyticsに送信
        }
    }
}

object DebugTree : Timber.DebugTree() {
    private val exclusionClasses: Set<KClass<*>> = setOf(
        DebugTree::class,
        Logger::class
    )

    override fun createStackElementTag(element: StackTraceElement): String? =
        Throwable().stackTrace.find { stackTraceElement ->
            isLoggableClassName(stackTraceElement.className)
        }!!.let { stackTraceElement ->
            with(stackTraceElement) { "($fileName:$lineNumber)#$methodName" }
        }

    private fun isLoggableClassName(className: String): Boolean =
        className.contains(BuildConfig.APPLICATION_ID) &&
                exclusionClasses.all { exclusionClass -> className != exclusionClass.java.name }
}