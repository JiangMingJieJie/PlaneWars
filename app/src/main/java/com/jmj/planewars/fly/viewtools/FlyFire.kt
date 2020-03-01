package com.jmj.planewars.fly.viewtools

import android.animation.ValueAnimator
import android.graphics.*
import android.view.View
import java.util.ArrayList

class FlyFire(private var rect: Rect, private var view: View) {
    private val particles = ArrayList<Particle>()
    private var borderWidth = 0
    private var paint = Paint()
    private var fireAnim: ValueAnimator? = null

    init {
        paint.color = Color.RED
    }

    /**
     * 绘制粒子
     */
    private fun draw(canvas: Canvas) {
        if (fireAnim == null) {
            fireAnim = ValueAnimator.ofInt(0, 1)
                .apply {
                    addUpdateListener {
                        view.invalidate()
                    }
                    repeatCount = ValueAnimator.INFINITE
                    start()
                }
        }

        particles.forEach {
            var h = rect.bottom - rect.top
            var yProgress = it.y - rect.top
            //粒子透明度
            paint.alpha = (255 - yProgress * (255F / h)).toInt()
            canvas.drawRect(
                it.x,
                it.y,
                it.x + borderWidth,
                it.y + borderWidth,
                paint
            )
        }
    }

    /**
     *计算粒子
     */
    fun drawParticle(canvas: Canvas) {
        borderWidth = (rect.right - rect.left) / 3
        if (borderWidth < 1) {
            borderWidth = 1
        }

        if (particles.size == 0) {
            addParticle((rect.right - rect.left) / borderWidth / 2 * 2)
        }
        //改变位置
        particles.forEach {
            if (it.xv > 0) {
                it.xv += 0.005f
            } else {
                it.xv += -0.005f
            }
            it.yv = it.yv + 0.2F

            it.x = it.x + it.xv
            it.y = it.y + it.yv
        }

        //定义一个临时数组遍历 删除其中已经走出屏幕的粒子
        val tempParticles = ArrayList<Particle>()
        tempParticles.addAll(particles)
        tempParticles.forEach {
            if (it.y > rect.bottom) {
                particles.remove(it)
            }
        }

        //每绘制一次添加一排粒子
        addParticle((rect.right - rect.left) / borderWidth / 2 * 2)
        draw(canvas)
    }

    /**
     * 添加粒子
     */
    private fun addParticle(count: Int) {
        for (i in 0 until count) {
            val molecule = (rect.right - rect.left)

            val particle =
                Particle(
                    rect.left + molecule * Math.random().toFloat() - borderWidth / 2,
                    rect.top.toFloat()
                )
            particle.xv = 0.5f - 1 * Math.random().toFloat()
            particles.add(particle)
        }
    }

    fun stopDrawParticle() {
        fireAnim?.cancel()
    }

    data class Particle(var x: Float, var y: Float, var xv: Float = 1F, var yv: Float = 1F)


}

