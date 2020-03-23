package jp.maskedronin.bitwatcher.presentation.common.resource

import android.content.Context
import androidx.annotation.StringRes

interface StringResource {
    fun apply(context: Context): String

    companion object {
        fun from(
            @StringRes resId: Int,
            vararg formatArgs: Any
        ): StringResource = object : StringResource {
            override fun apply(context: Context): String =
                context.getString(resId, *formatArgs)
        }

        fun from(message: String): StringResource = object : StringResource {
            override fun apply(context: Context): String = message
        }
    }
}

