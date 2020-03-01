package com.jmj.planewars.fly

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyfactory.FlyFactory
import com.jmj.planewars.fly.flyobject.Fly
import com.jmj.planewars.fly.flyobject.bullet.Bullet
import com.jmj.planewars.fly.flyobject.plane.Plane
import com.jmj.planewars.fly.view.MapView
import java.lang.Math.abs
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList


class FlyController(private var context: Activity, private var mapView: MapView) {
    /**
     * 我军飞机
     */
    private lateinit var gcdPlane: Plane
    /**
     * 敌军飞机集合
     */
    private var gmdPlanes: CopyOnWriteArrayList<Plane> = CopyOnWriteArrayList()
    /**
     * 我军子弹集合
     */
    private var gcdBullets: CopyOnWriteArrayList<Bullet> = CopyOnWriteArrayList()
    /**
     * 敌军子弹集合
     */
    private var gmdBullets: CopyOnWriteArrayList<Bullet> = CopyOnWriteArrayList()

    /**
     * 地图的宽高
     */
    private var w = 0
    private var h = 0

    private var random = Random()


    init {
        mapView.onMeasureFinishListener = object : MapView.OnViewLoadFinishListener {
            override fun onFinish(w: Int, h: Int) {
                this@FlyController.w = w
                this@FlyController.h = h
                createGcdPlane()
                createGmdPlane()

                Thread {
                    while (true) {
                        context.runOnUiThread {
                            shot(gcdPlane!!)
                        }

                        SystemClock.sleep(1000)
                    }
                }.start()

                Thread {
                    while (true) {
                        gmdPlanes.forEach {
                            context.runOnUiThread {
                                shot(it)
                            }
                        }
                        SystemClock.sleep(2000)

                    }
                }.start()
            }
        }
    }

    /**
     * 创建敌机
     */
    private fun createGmdPlane() {
        val gmdPlaneCount = random.nextInt(10) + 5
        for (i in 0 until gmdPlaneCount) {
            val plane = FlyFactory.getPlane(context, FlyType.PLANE_GMD)
            //随机布放位置
            plane.x = w * random.nextFloat()
            addFly(plane)
            moveFly(plane)
        }
    }

    /**
     * 添加我的飞机
     */
    private fun createGcdPlane() {
        //创建我的飞机
        gcdPlane = FlyFactory.getPlane(context, FlyType.PLANE_GCD)
        //指定我的飞机的位置
        gcdPlane.x = w / 2F - gcdPlane.w / 2
        gcdPlane.y = h - gcdPlane.h.toFloat()
        //添加到集合
        addFly(gcdPlane)
        //开启拖拽
        openFlyDrag(gcdPlane)
    }


    /**
     * 发射子弹
     */
    fun shot(plane: Plane) {
        val bullet = plane.shotBullet()!!
        addFly(bullet)
        moveFly(bullet)
    }

    /**
     * 移动fly
     */
    private fun moveFly(fly: Fly) {
        var start = fly.cy
        var end = if (fly.flyType == FlyType.BULLET_GCD || fly.flyType == FlyType.PLANE_GCD) {
            //我的子弹 自下而上
            -fly.h * 6F
        } else {
            //敌机子弹 自上而下
            h + fly.h * 6F
        }
        ValueAnimator.ofFloat(start, end)
            .apply {
                addUpdateListener {
                    //如果飞出屏幕 或者碰撞了之后导致销毁 则停止动画
                    if (fly.isBoom) {
                        cancel()
                        return@addUpdateListener
                    }

                    //移动Fly
                    fly.y = (it.animatedValue as Float)

                    //超出屏幕检测
                    if (checkFlyPosition(fly)) {
                        if (fly.flyType == FlyType.BULLET_GCD) {
                            //碰撞检测
                            checkFlyCollision(fly)
                        }
                    }

                }
                duration = (abs(start - end)).toLong() * fly.speed
                interpolator = LinearInterpolator()
                start()

            }
    }

    /**
     * fly位置检测 如果已经超出屏幕则删除
     */
    private fun checkFlyPosition(fly: Fly): Boolean {
        //如果view已经不再屏幕内了 删除它
        if (fly.x + fly.w <= 0 || fly.x >= w || fly.y + fly.h <= 0 || fly.y >= h) {
            removeFly(fly)
            return false
        }
        return true
    }

    /**
     * 飞行物碰撞检测
     */
    private fun checkFlyCollision(fly: Fly) {
        var temp = ArrayList<Plane>()
        temp.addAll(gmdPlanes)
        for (plane in temp) {
            //碰撞之后跳出循环
            if (isCollision(
                    fly.x,
                    fly.y,
                    fly.w.toFloat(),
                    fly.h.toFloat(),
                    plane.x,
                    plane.y,
                    plane.w.toFloat(),
                    plane.h.toFloat()
                )
            ) {
                removeFly(fly)
                removeFly(plane)
                break
            }
        }
    }

    /**
     * 碰撞检测
     */
    private fun isCollision(
        x1: Float,
        y1: Float,
        w1: Float,
        h1: Float,
        x2: Float,
        y2: Float,
        w2: Float,
        h2: Float
    ): Boolean {
        if (x1 > x2 + w2 || x1 + w1 < x2 || y1 > y2 + h2 || y1 + h1 < y2) {
            return false
        }
        return true
    }


    /**
     * 开启Fly的手势拖动
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun openFlyDrag(fly: Fly) {
        fly.flyView.setOnTouchListener(object : View.OnTouchListener {
            var dx = 0
            var dy = 0
            var mx = 0
            var my = 0
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dx = event.x.toInt()
                        dy = event.y.toInt()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        mx = event.x.toInt()
                        my = event.y.toInt()
                        move()
                    }
                    MotionEvent.ACTION_UP -> {

                    }
                }
                return true
            }

            private fun move() {
                fly.x += (mx - dx)
                fly.y += (my - dy)
            }
        })
    }


    /**
     * 添加Fly
     */
    private fun addFly(fly: Fly) {
        when (fly.flyType) {
            FlyType.BULLET_GCD -> {
                gcdBullets.add(fly as Bullet)
            }
            FlyType.BULLET_GMD -> {
                gmdBullets.add(fly as Bullet)
            }
            FlyType.PLANE_GMD -> {
                gmdPlanes.add(fly as Plane)
            }
        }
        mapView.addFly(fly)
    }

    /**
     * 删除fly
     */
    private fun removeFly(fly: Fly) {
        when (fly.flyType) {
            FlyType.BULLET_GCD -> {
                val bullet = fly as Bullet
                bullet.boom()
                gcdBullets.remove(bullet)
            }
            FlyType.BULLET_GMD -> {
                val bullet = fly as Bullet
                bullet.boom()
                gcdBullets.remove(bullet)
            }
            FlyType.PLANE_GMD -> {
                gmdPlanes.remove(fly)
            }
        }
        mapView.removeFly(fly)
    }


}