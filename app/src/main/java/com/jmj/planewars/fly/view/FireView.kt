package com.jmj.planewars.fly.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import com.jmj.planewars.fly.viewtools.FlyFire
import com.jmj.planewars.tools.dp2px

/**
 * 有火焰效果的基类View
 */
abstract class FireView : BaseView {
    private var flyFire: FlyFire? = null
    private var isReverse = false
    var horizontalMolecule = 0F
    var verticalMolecule = 0F
    var paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = dp2px(2).toFloat()
        color = Color.BLACK
        style = Paint.Style.STROKE
    }


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, isReverse: Boolean) : super(context) {
        this.isReverse = isReverse
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        computeRect()
        elevation = dp2px(10).toFloat()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            if (isReverse) {
                canvas.rotate(180F, w / 2, h / 2)
            }
            flyFire?.drawParticle(it)
        }
    }


    /**
     * 计算每块区域的具体位置
     */
    private fun computeRect() {
        var horizontalDenominator = 5
        var verticalDenominator = 5

        //横向每份的长度
        horizontalMolecule = w / horizontalDenominator
        //竖向每份的长度
        verticalMolecule = h / verticalDenominator

        var rect = Rect(
            (horizontalMolecule * 2).toInt(),
            (verticalMolecule * 4).toInt(),
            (horizontalMolecule * 3).toInt(),
            h.toInt()
        )
        flyFire = FlyFire(rect, this)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        flyFire?.stopDrawParticle()
    }

}