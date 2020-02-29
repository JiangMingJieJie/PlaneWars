package com.jmj.planewars.fly.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.graphics.toRect
import com.jmj.planewars.fly.viewtools.FlyFireDrawHelp

/**
 * 飞机
 */
class PlaneGmdView : FlyView {
    private var paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLUE
        style = Paint.Style.STROKE
    }

    private var flyFireDrawHelp: FlyFireDrawHelp? = null

    override fun drawHead(rect: RectF, w: Float, h: Float, canvas: Canvas) {
        val path = Path().apply {
            moveTo(rect.right - w / 2, rect.top)
            lineTo(rect.left, rect.bottom)
            lineTo(rect.right, rect.bottom)
            close()
        }

        canvas.drawPath(path, paint)
    }

    override fun drawBody(rect: RectF, w: Float, h: Float, canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }

    override fun drawTail(rect: RectF, w: Float, h: Float, canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }

    override fun drawFire(rect: RectF, w: Float, h: Float, canvas: Canvas) {
        if (flyFireDrawHelp == null) {
            flyFireDrawHelp = FlyFireDrawHelp(rect.toRect(), Color.BLUE)

            val ofInt = ValueAnimator.ofInt(0, 1)
            ofInt.addUpdateListener {
                invalidate()
            }
            ofInt.repeatCount = ValueAnimator.INFINITE
            ofInt.start()
        }

        flyFireDrawHelp?.drawParticle(canvas)
    }

    override fun drawLeftBodyWing(rect: RectF, w: Float, h: Float, canvas: Canvas) {
        var path = Path().apply {
            moveTo(rect.right, rect.top + h / 3)
            lineTo(rect.left, rect.bottom - h / 4)
            lineTo(rect.left, rect.bottom)
            lineTo(rect.right, rect.bottom)
            close()
        }
        canvas.drawPath(path, paint)

    }

    override fun drawRightBodyWing(rect: RectF, w: Float, h: Float, canvas: Canvas) {
        var path = Path().apply {
            moveTo(rect.left, rect.top + h / 3)
            lineTo(rect.right, rect.bottom - h / 4)
            lineTo(rect.right, rect.bottom)
            lineTo(rect.left, rect.bottom)
            close()
        }
        canvas.drawPath(path, paint)
    }

    override fun drawLeftTailWing(rect: RectF, w: Float, h: Float, canvas: Canvas) {
        var path = Path().apply {
            moveTo(rect.right, rect.top + h / 3)
            lineTo(
                rect.right - w / 2,
                rect.bottom - h / 4
            )
            lineTo(rect.right - w / 2, rect.bottom)
            lineTo(rect.right, rect.bottom)
            close()
        }
        canvas.drawPath(path, paint)
    }

    override fun drawRightTailWing(rect: RectF, w: Float, h: Float, canvas: Canvas) {
        var path = Path().apply {
            moveTo(rect.left, rect.top + h / 3)
            lineTo(rect.left + w / 2, rect.bottom - h / 4)
            lineTo(rect.left + w / 2, rect.bottom)
            lineTo(rect.left, rect.bottom)
            close()
        }
        canvas.drawPath(path, paint)
    }


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
}