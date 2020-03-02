package com.jmj.planewars.fly.view

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.jmj.planewars.fly.cons.FlyColors

/**
 * 飞机
 */
class PlaneGmdView : FireView {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        paint.style = Paint.Style.FILL
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        canvas?.let {
            paint.color = FlyColors.GMD_PLANE_BODY

            it.drawRoundRect(
                horizontalMolecule * 2,
                0F,
                horizontalMolecule * 3,
                verticalMolecule * 4,
                10F,
                10F,
                paint
            )
            paint.color =FlyColors.GMD_PLANE_WING

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
        }
    }
}