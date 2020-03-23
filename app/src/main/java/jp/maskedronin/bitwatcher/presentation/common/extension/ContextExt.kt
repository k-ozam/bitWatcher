package jp.maskedronin.bitwatcher.presentation.common.extension

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.resolveAttrResourceId(@AttrRes attrResourceId: Int): Int =
    TypedValue().also { typedValue ->
        theme.resolveAttribute(attrResourceId, typedValue, true)
    }.resourceId
