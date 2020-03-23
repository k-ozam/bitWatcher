package jp.maskedronin.bitwatcher.presentation.common

import android.widget.Toast
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource

data class ToastConfig(
    val message: StringResource,
    val duration: Duration
) {
    enum class Duration(private val value: Int) {
        SHORT(Toast.LENGTH_SHORT),
        LONG(Toast.LENGTH_LONG),
        ;

        fun toInt(): Int = value
    }
}