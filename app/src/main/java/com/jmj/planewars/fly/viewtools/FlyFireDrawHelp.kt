package com.jmj.planewars.fly.viewtools

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import java.util.ArrayList

class FlyFireDrawHelp(private var rect: Rect, color: Int) {
    private val particles = ArrayList<Particle>()
    private val particlesCount = 5
    private var borderWidth = 0
    private var paint = Paint()

    init {
        paint.color = color
    }

    /**
     * 绘制粒子
     */
    private fun draw(canvas: Canvas) {
        particles.forEach {
            //粒子透明度
            paint.alpha = 255 - (it.y * (255F / (rect.bottom - rect.top))).toInt()
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
            addParticle(particlesCount)
        }
        //改变位置
        particles.forEach {
            if (it.xv > 0) {
                it.xv += 0.01f
            } else {
                it.xv += -0.01f
            }
            it.yv = it.yv + 0.1f
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

    data class Particle(var x: Float, var y: Float, var xv: Float = 1F, var yv: Float = 1F)


}

