package com.jmj.planewars.fly.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt

/**
 * 飞机
 */
class PlaneView : FireView {
    /**
     * 遮罩颜色
     */
    private var maskColor = 0
    /**
     * 翅膀颜色
     */
    private var wingColor = 0
    /**
     * 躯干颜色
     */
    private var bodyColor = 0

    private var maskAnim: ValueAnimator? = null

    constructor(
        context: Context?, @ColorInt wingColor: Int, @ColorInt bodyColor: Int,
        isReverse: Boolean
    ) : super(
        context,
        isReverse
    ) {
        this.wingColor = wingColor
        this.bodyColor = bodyColor
    }


    init {
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            paint.color = bodyColor

            it.drawRoundRect(
                horizontalMolecule * 2,
                0F,
                horizontalMolecule * 3,
                verticalMolecule * 4,
                10F,
                10F,
                paint
            )
            paint.color = wingColor

            it.drawRoundRect(
                0F,
                verticalMolecule / 2,
                w,
                verticalMolecule + verticalMolecule / 2,
                10F,
                10F,
                paint
            )

            it.drawRoundRect(
                horizontalMolecule,
                verticalMolecule * 4 - verticalMolecule,
                w - horizontalMolecule,
                verticalMolecule * 4,
                10F,
                10F,
                paint
            )

            if (maskAnim != null) {
                paint.color = maskColor
                it.drawCircle(cx, cy, w.coerceAtMost(h) / 2 * 1.5F, paint)
                paint.alpha = 0xFF
            }
        }
    }

    /**
     * 开启遮罩
     */
    fun openMask() {
        if (maskAnim == null) {
            maskAnim =
                ValueAnimator.ofArgb(Color.parseColor("#55FFFFFF"), Color.parseColor("#11FFFFFF"))
                    .apply {
                        addUpdateListener {
                            maskColor = it.animatedValue as Int
                            invalidate()
                        }
                        repeatCount = ValueAnimator.INFINITE
                        repeatMode = ValueAnimator.REVERSE
                        duration = 1000
                        start()
                    }
        }

    }

    /**
     * 关闭遮罩
     */
    fun closeMask() {
        maskAnim?.cancel()
        maskAnim = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        closeMask()
    }
}