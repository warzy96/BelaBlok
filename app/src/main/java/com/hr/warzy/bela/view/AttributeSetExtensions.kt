package com.hr.warzy.bela.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleableRes

inline fun AttributeSet.withTypedArray(context: Context, @StyleableRes attrs: IntArray, block: TypedArray.() -> Unit) {
    with(context.obtainStyledAttributes(this, attrs, 0, 0)) {
        block()
        recycle()
    }
}

inline fun TypedArray.withResourceId(index: Int, block: (Int) -> Unit) {
    getResourceId(index, -1).let {
        if (it != -1) {
            block(it)
        }
    }
}

inline fun TypedArray.withIntDimension(index: Int, defaultValue: Int = 0, block: (Int) -> Unit) {
    block(getDimension(index, defaultValue.toFloat()).toInt())
}

inline fun TypedArray.withDimension(index: Int, defaultValue: Float = 0f, block: (Float) -> Unit) {
    block(getDimension(index, defaultValue))
}

inline fun TypedArray.withDimensionPixelSize(index: Int, defaultValue: Int = 0, block: (Int) -> Unit) {
    block(getDimensionPixelSize(index, defaultValue))
}

inline fun TypedArray.withInt(index: Int, invalidValue: Int = -1, block: (Int) -> Unit) {
    getInt(index, invalidValue).let {
        if (it != invalidValue) {
            block(it)
        }
    }
}

inline fun TypedArray.ifTrue(index: Int, block: () -> Unit) {
    getBoolean(index, false).let {
        if (it) block()
    }
}

inline fun TypedArray.withInterestingBoolean(index: Int, interestingValue: Boolean = true, block: () -> Unit) {
    getBoolean(index, !interestingValue).let {
        if (it == interestingValue) {
            block()
        }
    }
}

inline fun TypedArray.withColor(index: Int, defaultValue: Int = -1, block: (Int) -> Unit) {
    getColor(index, defaultValue).let {
        if (it != defaultValue) {
            block(it)
        }
    }
}

inline fun TypedArray.withAnyColor(index: Int, defaultValue: Int = -1, block: (Int) -> Unit) {
    block(getColor(index, defaultValue))
}

inline fun TypedArray.withString(index: Int, invokeIfNull: () -> Unit = {}, block: (String) -> Unit) {
    getString(index)?.let(block) ?: invokeIfNull()
}

inline fun TypedArray.withBoolean(index: Int, defaultValue: Boolean = false, block: (Boolean) -> Unit) {
    block(getBoolean(index, defaultValue))
}
