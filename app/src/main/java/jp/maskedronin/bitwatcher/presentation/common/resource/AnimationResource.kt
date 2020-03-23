package jp.maskedronin.bitwatcher.presentation.common.resource

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

class AnimationResource(@AnimRes private val resId: Int) {
    fun apply(context: Context): Animation =
        AnimationUtils.loadAnimation(context, resId)
}