package jp.maskedronin.bitwatcher.presentation.common.extension

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.canScrollDown(): Boolean = canScrollVertically(1)