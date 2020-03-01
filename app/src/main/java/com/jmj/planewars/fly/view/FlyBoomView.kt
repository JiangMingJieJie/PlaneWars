package com.jmj.planewars.fly.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.jmj.planewars.tools.dp2px
import com.jmj.planewars.tools.myLog
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 爆炸效果
 */
class FlyBoomView : View {
    private var w = 0F
    private var h = 0F
    private var cx = 0F
    private var cy = 0F

    var animatorListener: AnimatorListener? = null
    var paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = dp2px(2).toFloat()
        color = color
        style = Paint.Style.FILL
    }


    constructor(context: Context?, color: Int = Color.RED) : super(context) {
        paint.color = color
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var rects = ArrayList<Rect>()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (index in rects.indices) {
            canvas!!.drawRect(rects[index], paint)
            canvas.rotate(30F, cx, cy)
        }

    }


    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        w = measuredWidth.toFloat()
        h = measuredHeight.toFloat()
        cx = w / 2
        cy = h / 2
        computeRect()
        boomAnim()
    }

    fun boomAnim() {
        val animEnd = (sqrt(w.coerceAtLeast(h).toDouble().pow(2.0)) / 2).toInt()
        var oldAnimatedValue = 0
        ValueAnimator.ofInt(0, animEnd).apply {
            addUpdateListener {

                val animatedValue = it.animatedValue as Int
                paint.alpha = (255 - (animatedValue * (255F / (animEnd)))).toInt()

                for (index in rects.indices) {
                    val rect = rects[index]
                    rect.left += animatedValue - oldAnimatedValue
                    rect.right += animatedValue - oldAnimatedValue
                    rect.top += animatedValue - oldAnimatedValue
                    rect.bottom += animatedValue - oldAnimatedValue
                }
                oldAnimatedValue = animatedValue
                invalidate()
            }
            interpolator = LinearInterpolator()
            duration = 800
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    animatorListener?.onAnimationEnd()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
            start()
        }
    }

    private fun computeRect() {
        rects.clear()
        if (rects.size == 0) {
            var length = w.coerceAtMost(h) / 5
            for (i in 0..11) {
                rects.add(
                    Rect(
                        (cx - length / 2).toInt(),
                        (cy - length / 2).toInt(),
                        (cx + length / 2).toInt(),
                        (cy + length / 2).toInt()
                    )
                )
            }
        }
    }

    interface AnimatorListener {
        fun onAnimationEnd()
    }

}