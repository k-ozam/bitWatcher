package jp.maskedronin.bitwatcher.presentation.common

import android.view.View
import android.view.animation.Animation
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter

interface OnGroupClickListener {
    fun onGroupClick(group: Group)
}

@BindingAdapter("onAllClick")
fun Group.setAllOnClickListener(onGroupClickListener: OnGroupClickListener) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener {
            onGroupClickListener.onGroupClick(this)
        }
    }
}

@BindingAdapter("allEnabled")
fun Group.setAllEnabled(enable: Boolean) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).isEnabled = enable
    }
}

@BindingAdapter("animation")
fun View.setAnimationCompat(animation: Animation?) {
    if (animation == null) {
        clearAnimation()
    } else {
        startAnimation(animation)
    }
}