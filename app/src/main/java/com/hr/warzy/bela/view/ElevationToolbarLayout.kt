package com.hr.warzy.bela.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hr.warzy.bela.R

private const val TOOLBAR_INDEX = 0
private const val CHILD_VIEW_INDEX = 1

/**
 * This layout changes the elevation of the toolbar (first child) based on the scroll state from the second child.
 * If the second child is not scrolled elevation is 0, if it is, elevation is 4dp
 */
class ElevationToolbarLayout : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        clipToPadding = false
        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (isInEditMode) {
            return
        }

        val toolbar = getChildAt(TOOLBAR_INDEX) ?: throw IllegalStateException("First child (toolbar) missing")
        val elevationPx = resources.getDimension(R.dimen.elevation_toolbar_elevation)

        var child = getChildAt(CHILD_VIEW_INDEX)
        if (child is SwipeRefreshLayout) child = child.getChildAt(1)

        child?.let {
            when (it) {
                is RecyclerViewWithScrollState -> it.addOnVerticalScrollChangedListener { scrollY -> scrollChanged(toolbar, scrollY, elevationPx) }
                is WithScrollState -> it.setListener { scrollY -> scrollChanged(toolbar, scrollY, elevationPx) }
                else -> throw IllegalStateException("Second child has unsupported view type")
            }
        } ?: throw IllegalStateException("Missing second (scrollable) child")
    }

    private fun scrollChanged(toolbar: View, scrollY: Int, maxElevation: Float) {
        toolbar.elevation = Math.min(scrollY.toFloat(), maxElevation)
    }
}

open class ScrollViewWithScrollState : ScrollView, WithScrollState {

    private var listener: ((Int) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setListener(listener: (Int) -> Unit) {
        this.listener = listener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        listener?.invoke(t)
    }
}

open class NestedScrollViewWithScrollState : NestedScrollView, WithScrollState {

    private var listener: ((Int) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setListener(listener: (Int) -> Unit) {
        this.listener = listener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        listener?.invoke(t)
    }
}

interface WithScrollState {
    fun setListener(listener: (Int) -> Unit)
}
