package com.jmj.planewars.fly.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.jmj.planewars.fly.cons.FlyColors.GCD_PLANE_BODY
import com.jmj.planewars.fly.cons.FlyColors.GCD_PLANE_WING

/**
 * 飞机
 */
class PlaneGcdView : FireView {

    private var maskColor = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        paint.style = Paint.Style.FILL
        maskAnim()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            paint.color = GCD_PLANE_BODY

            it.drawRoundRect(
                horizontalMolecule * 2,
                0F,
                horizontalMolecule * 3,
                verticalMolecule * 4,
                10F,
                10F,
                paint
            )
            paint.color = GCD_PLANE_WING

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

            if (maskColor != 0) {
                paint.color = maskColor
                it.drawCircle(cx, cy, w.coerceAtMost(h) / 2 * 1.5F, paint)
                paint.alpha = 0xFF
            }
        }
    }

    fun maskAnim() {
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