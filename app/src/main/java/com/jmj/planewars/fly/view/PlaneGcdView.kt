package com.jmj.planewars.fly.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi

/**
 * 飞机
 */
class PlaneGcdView : FlyView {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        paint.style = Paint.Style.FILL
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
        }
    }
}