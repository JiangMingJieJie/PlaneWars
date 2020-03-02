package com.jmj.planewars.fly.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.jmj.planewars.fly.cons.FlyColors
import com.jmj.planewars.fly.flyobject.Fly

/**
 * 飞机
 */
class BulletGmdView : FireView {


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            paint.color = FlyColors.GMD_BULLET

            it.drawRoundRect(
                horizontalMolecule * 2,
                0F,
                horizontalMolecule * 3,
                verticalMolecule * 4,
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

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
}