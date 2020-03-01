package com.jmj.planewars.fly.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.jmj.planewars.fly.viewtools.FlyFire
import com.jmj.planewars.tools.dp2px


abstract class FlyView : View {
    var w = 0F
    var h = 0F
    var cx = 0F
    var cy = 0F
    private var flyFire: FlyFire? = null

    var isReverse = false
        set(value) {
            if (value != field) {
                postInvalidate()
            }
            field = value
        }

    var paint = Paint().apply {
        isAntiAlias = true
        strokeWidth= dp2px(2).toFloat()
        color = Color.BLACK
        style = Paint.Style.STROKE
    }


    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        w = measuredWidth.toFloat()
        h = measuredHeight.toFloat()
        cx = w / 2
        cy = h / 2
        computeRect()
    }

    /**
     * TODO 爆炸效果
     */
    fun boom() {

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

    var horizontalMolecule = 0F
    var verticalMolecule = 0F
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