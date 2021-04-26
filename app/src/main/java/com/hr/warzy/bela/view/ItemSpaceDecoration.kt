package com.hr.warzy.bela.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpaceDecoration(private val spaceInPx: Int, private val isVertical: Boolean = true, private val hasFirstItemSpace: Boolean = true) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) return

        outRect.set(
            if (hasFirstItemSpace && itemPosition == 0 && !isVertical) spaceInPx else 0,
            if (hasFirstItemSpace && itemPosition == 0 && isVertical) spaceInPx else 0,
            if (!isVertical) spaceInPx else 0,
            if (isVertical) spaceInPx else 0
        )
    }
}