package com.jmj.planewars.fly.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnRepeat
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 滚动的星空背景
 */
class ScrollStarsView : BaseView {
    /**
     * 圆形集合
     */
    private var circleStars = CopyOnWriteArrayList<CircleStar>()
    /**
     * 圆形移动动画
     */
    private var moveRectAnim: ValueAnimator? = null

    private var random = Random()
    /**
     * 圆形直径
     */
    private var diameter = 0

    private var paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL

    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        diameter = (w / 3).toInt()
        addRect()
        moveRect()
    }

    /**
     * 添加圆形
     */
    private fun addRect() {
        var rdX = (random.nextInt(4) * diameter).toFloat()
        var rdY = -diameter.toFloat()
        var rdR = diameter / 2F * (random.nextFloat() + 0.1F)
        circleStars.add(
            CircleStar(
                rdX,
                rdY,
                rdR
            )
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { c ->
            /**
             * 绘制背景矩形
             */
            paint.color = Color.parseColor("#0F2332")
            c.drawRect(0F, 0F, w, h, paint)

            /**
             * 绘制圆形
             */
            paint.color = Color.parseColor("#374060")
            circleStars.forEach {
                c.drawCircle(it.x, it.y, it.r, paint)
            }
        }
    }


    /**
     *  移动圆形  每走diameter*2的距离 触发一次添加
     *
     */
    private fun moveRect() {
        if (moveRectAnim == null) {
            var oldAnimateValue = 0
            moveRectAnim = ValueAnimator.ofInt(1, diameter * 2)
                .apply {
                    addUpdateListener { it ->
                        val animateValue = it.animatedValue as Int

                        //repeat的时候重置oldAnimateValue
                        if (oldAnimateValue > animateValue) {
                            oldAnimateValue = 0
                            addRect()
                        }

                        circleStars.forEach {
                            it.y += animateValue - oldAnimateValue
                            //如果已经走出了屏幕外则删除它
                            if (it.y > h + diameter) {
                                circleStars.remove(it)
                            }
                        }

                        oldAnimateValue = animateValue
                        invalidate()
                    }

                    repeatCount = ValueAnimator.INFINITE
                    interpolator = LinearInterpolator()
                    duration = 2000
                    start()
                }
        }
    }


    data class CircleStar(var x: Float, var y: Float, var r: Float)


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        moveRectAnim?.cancel()
        moveRectAnim = null
    }
}