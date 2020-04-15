package jp.maskedronin.bitwatcher.presentation.common.extension

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.google.android.material.snackbar.Snackbar
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.presentation.common.SnackbarConfig
import jp.maskedronin.bitwatcher.presentation.common.ToastConfig
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource

fun Activity.makeSnackbar(
    view: View? = null,
    config: SnackbarConfig
): Snackbar = Snackbar.make(
    view ?: requireView(),
    config.message.apply(this),
    config.duration.toInt()
).apply {
    setAction(R.string.detail) {
        dismiss()
        createMessageDialog(config.message)
            .show()
    }
}

fun Activity.makeToast(config: ToastConfig): Toast =
    Toast.makeText(this, config.message.apply(this), config.duration.toInt())

fun Activity.createMessageDialog(message: StringResource): Dialog =
    AlertDialog.Builder(this)
        .setMessage(message.apply(this))
        .setNegativeButton(R.string.close, null)
        .create()

fun Activity.requireView(): View = window.findViewById<ViewGroup>(android.R.id.content)[0]
