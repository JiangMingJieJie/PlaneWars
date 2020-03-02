package com.jmj.planewars.fly.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.annotation.ColorInt

/**
 * 子弹
 */
class BulletView : FireView {

    /**
     * 子弹颜色
     */
    private var bulletColor = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            paint.color = bulletColor

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

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, @ColorInt bulletColor: Int, isReverse: Boolean) : super(
        context,
        isReverse
    ) {
        this.bulletColor = bulletColor
    }
}