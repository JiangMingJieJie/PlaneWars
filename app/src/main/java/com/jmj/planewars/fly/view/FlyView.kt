package com.jmj.planewars.fly.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toRect
import androidx.core.graphics.toRectF
import com.jmj.planewars.fly.viewtools.FlyFireDrawHelp


abstract class FlyView : View {

    /**
     * 将飞行物划分为几大块
     * 头部
     * 主体
     * 尾部
     * 尾焰
     * 左翅膀
     * 右翅膀
     */
    private lateinit var headRect: Rect
    private lateinit var bodyRect: Rect
    private lateinit var tailRect: Rect
    private lateinit var fireRect: Rect
    private lateinit var leftBodyWingRect: Rect
    private lateinit var rightBodyWingRect: Rect
    private lateinit var leftTailWingRect: Rect
    private lateinit var rightTailWingRect: Rect

    private var w = 0F
    private var h = 0F
    private var cx = 0F
    private var cy = 0F
    private var fireAnim: ValueAnimator? = null


    var isReverse = false
        set(value) {
            if (value != field) {
                postInvalidate()
            }
            field = value
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
            drawHead(
                headRect.toRectF(),
                (headRect.right - headRect.left).toFloat(),
                (headRect.bottom - headRect.top).toFloat(),
                it
            )
            drawBody(
                bodyRect.toRectF(),
                (bodyRect.right - bodyRect.left).toFloat(),
                (bodyRect.bottom - bodyRect.top).toFloat(),
                it
            )
            drawTail(
                tailRect.toRectF(),
                (tailRect.right - tailRect.left).toFloat(),
                (tailRect.bottom - tailRect.top).toFloat(),
                it
            )

            drawLeftBodyWing(
                leftBodyWingRect.toRectF(),
                (leftBodyWingRect.right - leftBodyWingRect.left).toFloat(),
                (leftBodyWingRect.bottom - leftBodyWingRect.top).toFloat(),
                it
            )
            drawRightBodyWing(
                rightBodyWingRect.toRectF(),
                (rightBodyWingRect.right - rightBodyWingRect.left).toFloat(),
                (rightBodyWingRect.bottom - rightBodyWingRect.top).toFloat(),
                it
            )
            drawLeftTailWing(
                leftTailWingRect.toRectF(),
                (leftTailWingRect.right - leftTailWingRect.left).toFloat(),
                (leftTailWingRect.bottom - leftTailWingRect.top).toFloat(),
                it
            )
            drawRightTailWing(
                rightTailWingRect.toRectF(),
                (rightTailWingRect.right - rightTailWingRect.left).toFloat(),
                (rightTailWingRect.bottom - rightTailWingRect.top).toFloat(),
                it
            )

            drawFire(
                fireRect.toRectF(),
                (fireRect.right - fireRect.left).toFloat(),
                (fireRect.bottom - fireRect.top).toFloat(),
                it
            )
        }
    }


    /**
     * 计算每块区域的具体位置
     */
    private fun computeRect() {
        //横向分五份
        var horizontalDenominator = 5
        //竖向分四份
        var verticalDenominator = 4
        //横向每份的长度
        var horizontalMolecule = w / horizontalDenominator
        //竖向每份的长度
        var verticalMolecule = h / verticalDenominator



        headRect = Rect(
            (horizontalMolecule * 2).toInt(),
            0,
            (horizontalMolecule * 3).toInt(),
            (verticalMolecule * 1).toInt()
        )

        bodyRect = Rect(
            headRect.left,
            headRect.bottom,
            headRect.right,
            (headRect.bottom + verticalMolecule).toInt()
        )

        tailRect = Rect(
            bodyRect.left,
            bodyRect.bottom,
            bodyRect.right,
            (bodyRect.bottom + verticalMolecule).toInt()
        )

        fireRect = Rect(
            bodyRect.left,
            tailRect.bottom,
            bodyRect.right,
            (tailRect.bottom + verticalMolecule).toInt()
        )

        leftBodyWingRect = Rect(
            0,
            bodyRect.top,
            (horizontalMolecule * 2).toInt(),
            bodyRect.bottom
        )

        rightBodyWingRect = Rect(
            (w - horizontalMolecule * 2).toInt(),
            bodyRect.top,
            w.toInt(),
            bodyRect.bottom
        )

        leftTailWingRect = Rect(
            0,
            tailRect.top,
            (horizontalMolecule * 2).toInt(),
            tailRect.bottom
        )

        rightTailWingRect = Rect(
            (w - horizontalMolecule * 2).toInt(),
            tailRect.top,
            w.toInt(),
            tailRect.bottom
        )
    }


    /**
     * 绘制头部
     */
    abstract fun drawHead(rect: RectF, w: Float, h: Float, canvas: Canvas)

    /**
     * 绘制主体
     */
    abstract fun drawBody(rect: RectF, w: Float, h: Float, canvas: Canvas)

    /**
     * 绘制尾部
     */
    abstract fun drawTail(rect: RectF, w: Float, h: Float, canvas: Canvas)

    /**
     * 绘制尾焰
     */
    abstract fun drawFire(rect: RectF, w: Float, h: Float, canvas: Canvas)

    /**
     * 绘制左翅膀
     */
    abstract fun drawLeftBodyWing(rect: RectF, w: Float, h: Float, canvas: Canvas)

    /**
     * 绘制右翅膀
     */
    abstract fun drawRightBodyWing(rect: RectF, w: Float, h: Float, canvas: Canvas)

    /**
     * 绘制左翅膀
     */
    abstract fun drawLeftTailWing(rect: RectF, w: Float, h: Float, canvas: Canvas)

    /**
     * 绘制右翅膀
     */
    abstract fun drawRightTailWing(rect: RectF, w: Float, h: Float, canvas: Canvas)


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        fireAnim?.cancel()
    }

}