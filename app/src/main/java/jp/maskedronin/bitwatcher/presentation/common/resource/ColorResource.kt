package jp.maskedronin.bitwatcher.presentation.common.resource

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import jp.maskedronin.bitwatcher.presentation.common.extension.resolveAttrResourceId

interface ColorResource {
    @ColorInt
    fun apply(context: Context): Int

    companion object {
        fun from(@ColorRes resId: Int): ColorResource = object :
            ColorResource {
            override fun apply(context: Context): Int =
                ResourcesCompat.getColor(context.resources, resId, null)
        }

        fun fromAttr(@AttrRes resId: Int) = object :
            ColorResource {
            override fun apply(context: Context): Int =
                ResourcesCompat.getColor(
                    context.resources,
                    context.resolveAttrResourceId(resId),
                    context.theme
                )
        }
    }
}