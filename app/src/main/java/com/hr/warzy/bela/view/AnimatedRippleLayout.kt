package com.hr.warzy.bela.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Region
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.FrameLayout
import androidx.annotation.Nullable
import com.hr.warzy.bela.R
import kotlin.math.pow
import kotlin.math.sqrt

private const val HALF_DIVIDER = 2

private const val REQUIRED_NUMBER_OF_CHILDREN = 2

private const val RADIUS_ANIMATOR_START = 0f
private const val RADIUS_ANIMATOR_END = 100f
private const val DEFAULT_ANIMATION_DURATION = 200

private const val SQUARE = 2.0

/**
 * This class is used to transition between (un)checked state.
 * It transitions between two child views (each child for one state).
 *
 * Background child is for unchecked state
 */
class AnimatedRippleLayout : FrameLayout, Checkable {

    private var onCheckedChangedListener: (Boolean) -> Unit = {}
    private var middleX: Float = 0f
    private var middleY: Float = 0f
    private var circleRadius: Float = 0f
    private var maxCircleRadius: Float = 0f

    private var rippleAnimationDuration: Long = DEFAULT_ANIMATION_DURATION.toLong()

    private var isChecked: Boolean = false

    /**
     * Foreground child -> checked state
     * Background child -> unchecked state
     */
    private lateinit var foregroundChild: View
    private lateinit var backgroundChild: View

    private lateinit var rippleAnimator: ValueAnimator
    private var regionOp: Region.Op = Region.Op.INTERSECT

    private var animateRippleFunction: (Boolean) -> Unit = ::animateExpandCollapseRipple
    private var initAnimatorFunction: () -> Unit = ::initExpandCollapseRippleAnimator

    constructor(context: Context) : super(context) {
        init(null, 0, 0)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(attributeSet, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr, 0)
    }

    private fun init(@Nullable attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        attrs?.withTypedArray(context, R.styleable.AnimatedRippleLayout) {
            withInt(R.styleable.AnimatedRippleLayout_rippleAnimationDuration, DEFAULT_ANIMATION_DURATION) { rippleAnimationDuration = it.toLong() }
            withInt(R.styleable.AnimatedRippleLayout_rippleStyle) {
                when (it) {
                    RippleStyle.EXPAND_COLLAPSE.value -> initAnimatorFunction = ::initExpandCollapseRippleAnimator
                    RippleStyle.EXPAND_EXPAND.value -> initAnimatorFunction = ::initExpandExpandRippleAnimator
                    else -> initAnimatorFunction = ::initExpandCollapseRippleAnimator
                }
            }
        }

        isClickable = true
        isFocusable = true
        initAnimatorFunction()
        setOnClickListener { toggle() }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if (childCount != REQUIRED_NUMBER_OF_CHILDREN) throw RuntimeException("AnimatedRippleLayout must contain exactly two child views!")

        backgroundChild = getChildAt(0)
        foregroundChild = getChildAt(1)
    }

    /**
     * First draw the background child (unselected state).
     * Limit the region of drawing with [canvas.clipPath] function using current [regionOp].
     * On top of that child draw the foreground child (selected state) within the limits of clipPath.
     * The animation is done using [rippleAnimator].
     */
    override fun dispatchDraw(canvas: Canvas) {
        drawChild(canvas, backgroundChild, 0)

        canvas.save()
        canvas.clipPath(Path().apply {
            addCircle(middleX, middleY, circleRadius, Path.Direction.CW)
        }, regionOp)
        drawChild(canvas, foregroundChild, 0)
        canvas.restore()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        middleY = paddingTop + (height - paddingBottom - paddingTop) / HALF_DIVIDER.toFloat()
        middleX = paddingLeft + (width - paddingLeft - paddingRight) / HALF_DIVIDER.toFloat()
        maxCircleRadius = sqrt(middleX.toDouble().pow(SQUARE) + middleY.toDouble().pow(SQUARE)).toFloat()
    }

    private fun initRippleAnimator() {
        rippleAnimator = ValueAnimator.ofFloat(RADIUS_ANIMATOR_START, RADIUS_ANIMATOR_END).apply {
            addUpdateListener {
                circleRadius = maxCircleRadius * animatedFraction
                invalidate()
            }
            duration = rippleAnimationDuration
        }
    }

    private fun initExpandExpandRippleAnimator() {
        initRippleAnimator()
        animateRippleFunction = ::animateExpandExpandRipple
    }

    private fun initExpandCollapseRippleAnimator() {
        initRippleAnimator()
        animateRippleFunction = ::animateExpandCollapseRipple
    }

    fun setRippleAnimationDuration(duration: Long) {
        rippleAnimationDuration = duration
    }

    private fun animateExpandExpandRipple(isChecked: Boolean) {
        if (!rippleAnimator.isRunning) {
            rippleAnimator.start()
            regionOp = if (isChecked) Region.Op.INTERSECT else Region.Op.DIFFERENCE
        } else {
            rippleAnimator.reverse()
        }
    }

    private fun animateExpandCollapseRipple(isChecked: Boolean) {
        if (isChecked) rippleAnimator.start() else rippleAnimator.reverse()
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun setChecked(checked: Boolean) {
        if (this.isChecked == checked) return

        this.isChecked = checked

        onCheckedChangedListener(isChecked)
        animateRippleFunction(isChecked)
    }

    fun setOnCheckedChangedListener(onCheckedChangeListener: (Boolean) -> Unit) {
        this.onCheckedChangedListener = onCheckedChangeListener
    }

    private enum class RippleStyle(val value: Int) {
        EXPAND_EXPAND(0),
        EXPAND_COLLAPSE(1)
    }
}
