package com.jmj.planewars.fly.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.jmj.planewars.tools.myLog

/**
 * 飞机
 */
class PlaneGcdView : FlyView {

    private var maskColor = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        paint.style = Paint.Style.FILL
        maskAnim()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            paint.color = Color.parseColor("#D81B60")

            it.drawRoundRect(
                horizontalMolecule * 2,
                0F,
                horizontalMolecule * 3,
                verticalMolecule * 4,
                10F,
                10F,
                paint
            )
            paint.color = Color.parseColor("#008577")

            it.drawRoundRect(
                0F,
                verticalMolecule / 2,
                w,
                verticalMolecule + verticalMolecule / 2,
                10F,
                10F,
                paint
            )
            paint.color = Color.parseColor("#008577")


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
                paint.alpha = 0x33
                it.drawCircle(cx, cy, w.coerceAtMost(h) / 2 * 1.5F, paint)
                paint.alpha = 0xFF
            }
        }
    }

    fun maskAnim() {
        ValueAnimator.ofArgb(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE)
            .apply {
                addUpdateListener {
                    maskColor = it.animatedValue as Int
                    invalidate()
                }
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                duration = 4000
                start()
            }


    }
}