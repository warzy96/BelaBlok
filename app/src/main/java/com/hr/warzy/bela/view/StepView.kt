package com.hr.warzy.bela.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.card.MaterialCardView
import com.hr.warzy.bela.R
import kotlinx.android.synthetic.main.increment_decrement_button_layout.view.*

class StepView : MaterialCardView {

    private var onValueChangeListener: (Int) -> Unit = {}

    var currentValue: Int
        get() = value.text.toString().toInt()
        set(v) {
            value.text = if (v > 99 || v < 0) 0.toString() else v.toString()
            onValueChangeListener(v)
        }

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.increment_decrement_button_layout, this, true)

        attrs?.withTypedArray(context, R.styleable.StepView) {
            withString(R.styleable.StepView_step_title, { title.visibility = View.GONE }) { title.text = it }
            withInt(R.styleable.StepView_initial_value, 0) { value.setText(it) }
        }

        cardElevation = resources.getDimensionPixelSize(R.dimen.turn_card_elevation).toFloat()
        radius = resources.getDimensionPixelSize(R.dimen.turn_card_corner_radius).toFloat()

        increment.setOnClickListener { increment() }
        decrement.setOnClickListener { decrement() }
    }

    private fun increment() {
        val v = value.text.toString().toInt()
        if (v == 99) return

        value.text = value.text.toString().toInt().inc().toString()
        onValueChangeListener(v)
    }

    private fun decrement() {
        val v = value.text.toString().toInt()
        if (v == 0) return

        value.text = v.dec().toString()
        onValueChangeListener(v)
    }

    fun setOnValueChangedListener(onValueChangeListener: (Int) -> Unit) {
        this.onValueChangeListener = onValueChangeListener
    }
}