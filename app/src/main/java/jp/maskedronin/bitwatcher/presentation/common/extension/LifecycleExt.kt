package jp.maskedronin.bitwatcher.presentation.common.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

fun Lifecycle.observeEvent(
    targetEvent: Lifecycle.Event,
    predicate: () -> Unit
) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == targetEvent) {
                predicate()
            }
        }
    })
}