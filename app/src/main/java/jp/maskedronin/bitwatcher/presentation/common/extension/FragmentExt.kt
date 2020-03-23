package jp.maskedronin.bitwatcher.presentation.common.extension

import android.app.Dialog
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import jp.maskedronin.bitwatcher.presentation.common.SnackbarConfig
import jp.maskedronin.bitwatcher.presentation.common.ToastConfig
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource

fun Fragment.makeSnackbar(view: View? = null, config: SnackbarConfig): Snackbar =
    requireActivity().makeSnackbar(view, config)

fun Fragment.makeToast(config: ToastConfig): Toast = requireActivity().makeToast(config)

fun Fragment.createMessageDialog(message: StringResource): Dialog =
    requireActivity().createMessageDialog(message)
