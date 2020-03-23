package jp.maskedronin.bitwatcher.presentation.common

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(
    @Px
    private val verticalMargin: Int,
    @Px
    private val horizontalMargin: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.apply {
            top = verticalMargin
            bottom = verticalMargin
            left = horizontalMargin
            right = horizontalMargin
        }
    }
}