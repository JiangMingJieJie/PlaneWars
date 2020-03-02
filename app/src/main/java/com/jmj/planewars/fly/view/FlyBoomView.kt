package com.jmj.planewars.fly.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import com.jmj.planewars.tools.dp2px
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 爆炸效果
 */
class FlyBoomView : BaseView {
    /**
     * 方块集合
     */
    private var rects = ArrayList<Rect>()
    /**
     * 爆炸动画时间
     */
    private var BOOM_DURATION = 500L
    /**
     * 爆炸动画
     */
    var animatorListener: AnimatorListener? = null

    private var paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = dp2px(2).toFloat()
        color = Color.RED
        style = Paint.Style.FILL
    }

    constructor(context: Context?,  @ColorInt boomColor: Int ) : super(context) {
        paint.color = boomColor
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    /**
     * 所有的方块实际都朝一个方向移动,但是每移动一个旋转30°,造成圆形扩散效果
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (index in rects.indices) {
            canvas!!.drawRect(rects[index], paint)
            canvas.rotate(30F, cx, cy)
        }

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        w = measuredWidth.toFloat()
        h = measuredHeight.toFloat()
        cx = w / 2
        cy = h / 2
        computeRect()
        boomAnim()
    }

    /**
     * 移动方块动画
     */
    private fun boomAnim() {
        val animEnd = (sqrt(w.coerceAtLeast(h).toDouble().pow(2.0)) / 2).toInt()
        var oldAnimatedValue = 0
        ValueAnimator.ofInt(0, (animEnd * 1.5F).toInt()).apply {
            addUpdateListener {

                val animatedValue = it.animatedValue as Int
                paint.alpha = (255 - (animatedValue * (255F / (animEnd * 1.5F)))).toInt()

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
            duration = BOOM_DURATION
            doOnEnd {
                animatorListener?.onAnimationEnd()
            }
            start()
        }
    }

    /**
     * 添加12个方块在View的中心
     */
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