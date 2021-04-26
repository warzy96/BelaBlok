package com.hr.warzy.bela.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

private typealias OnScrollChangedListener = (Int) -> Unit

open class RecyclerViewWithScrollState : RecyclerView {

    private var onVerticalScrollChangedListeners = mutableListOf<OnScrollChangedListener>()
    private var onHorizontalScrollChangedListeners = mutableListOf<OnScrollChangedListener>()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    init {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) = publishScrollChanged()
        })
    }

    final override fun addOnScrollListener(listener: OnScrollListener) {
        super.addOnScrollListener(listener)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)

        adapter?.registerAdapterDataObserver(object : AdapterDataObserver() {

            override fun onChanged() {
                super.onChanged()
                post(this@RecyclerViewWithScrollState::publishScrollChanged)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                post(this@RecyclerViewWithScrollState::publishScrollChanged)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                post(this@RecyclerViewWithScrollState::publishScrollChanged)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                post(this@RecyclerViewWithScrollState::publishScrollChanged)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                post(this@RecyclerViewWithScrollState::publishScrollChanged)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                super.onItemRangeChanged(positionStart, itemCount, payload)
                post(this@RecyclerViewWithScrollState::publishScrollChanged)
            }
        })
    }

    fun addOnVerticalScrollChangedListener(onVerticalScrollChangedListener: OnScrollChangedListener) {
        onVerticalScrollChangedListeners.add(onVerticalScrollChangedListener)
    }

    fun addOnHorizontalScrollChangedListener(onHorizontalScrollChangedListener: OnScrollChangedListener) {
        onHorizontalScrollChangedListeners.add(onHorizontalScrollChangedListener)
    }

    private fun publishScrollChanged() {
        if (onVerticalScrollChangedListeners.isNotEmpty()) {
            val verticalScrollOffset = computeVerticalScrollOffset()
            onVerticalScrollChangedListeners.forEach { it(verticalScrollOffset) }
        }

        if (onHorizontalScrollChangedListeners.isNotEmpty()) {
            val horizontalScrollOffset = computeHorizontalScrollOffset()
            onHorizontalScrollChangedListeners.forEach { it(horizontalScrollOffset) }
        }
    }
}
