package jp.maskedronin.bitwatcher.presentation.common

import com.google.android.material.snackbar.Snackbar
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource

data class SnackbarConfig(
    val message: StringResource,
    val duration: Duration
) {
    enum class Duration(private val value: Int) {
        SHORT(Snackbar.LENGTH_SHORT),
        LONG(Snackbar.LENGTH_LONG),
        INDEFINITE(Snackbar.LENGTH_INDEFINITE)
        ;

        fun toInt(): Int = value
    }
}