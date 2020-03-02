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
import com.jmj.planewars.fly.flyobject.plane.Plane
import com.jmj.planewars.fly.view.FlyBoomView
import com.jmj.planewars.fly.view.MapView
import com.jmj.planewars.tools.myLog
import java.lang.Math.abs
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


class FlyController(private var activity: Activity, private var mapView: MapView) {
    /**
     * 我军飞机
     */
    private var gcdPlanes: CopyOnWriteArrayList<Fly> = CopyOnWriteArrayList()
    /**
     * 敌军飞机集合
     */
    private var gmdPlanes: CopyOnWriteArrayList<Fly> = CopyOnWriteArrayList()
    /**
     * 我军子弹集合
     */
    private var gcdBullets: CopyOnWriteArrayList<Fly> = CopyOnWriteArrayList()
    /**
     * 敌军子弹集合
     */
    private var gmdBullets: CopyOnWriteArrayList<Fly> = CopyOnWriteArrayList()
    /**
     * 屏幕内的敌军飞机数量上限
     */
    private var gmdPlaneCount = 10
    /**
     * 地图的宽高
     */
    private var w = 0
    private var h = 0

    private var isGameOver = false
    private var isQuitGame = false

    private var random = Random()

    var onGameOverListener: OnGameOverListener? = null


    init {
        /**
         * mapView测量完成
         */
        mapView.onMeasureFinishListener = object : MapView.OnViewLoadFinishListener {
            override fun onFinish(w: Int, h: Int) {
                this@FlyController.w = w
                this@FlyController.h = h
                addGcdPlane()
                addGmdPlane()
                timerBoss()
                startGmdPlaneShotThread()
            }
        }
    }

    /**
     * 我的飞机自动发射子弹线程
     */
    private fun startGcdPlaneShotThread() {
        Thread {
            while (!isGameOver) {
                activity.runOnUiThread {
                    if (gcdPlanes.size != 0) {
                        shot(gcdPlanes[0] as Plane)
                    }
                }

                SystemClock.sleep(200)
            }
        }.start()
    }

    /**
     * 敌机自动发射子弹线程
     */
    private fun startGmdPlaneShotThread() {
        Thread {
            while (!isQuitGame) {
                gmdPlanes.forEach {
                    activity.runOnUiThread {
                        shot(it as Plane)
                    }
                }
                SystemClock.sleep(4000)

            }
        }.start()
    }

    fun reStart() {
        isGameOver = false
        addGcdPlane()
    }

    /**
     * 添加我的飞机
     */
    fun addGcdPlane() {
        //创建我的飞机
        var gcdPlane = FlyFactory.getPlane(activity, FlyType.PLANE_GCD)
        //指定我的飞机的位置
        gcdPlane.x = w / 2F - gcdPlane.w / 2
        gcdPlane.y = h - gcdPlane.h.toFloat()
        //添加到集合
        addFly(gcdPlane)
        //开启拖拽
        openFlyDrag(gcdPlane)

        startGcdPlaneShotThread()

    }

    private fun timerBoss() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                activity.runOnUiThread {
                    createGmdPlane(FlyType.BOSS)
                }
            }
        }, 10000)
    }


    /**
     * 创建敌机Fly
     */
    private fun createGmdPlane(flyType: FlyType) {
        val plane = FlyFactory.getPlane(activity, flyType)
        //随机布放位置
        plane.x = (w - plane.w) * random.nextFloat()
        plane.y = (-plane.w).toFloat()
        addFly(plane)
        moveFly(plane)
    }

    /**
     * 创建爆炸效果的fly
     */
    private fun createBoom(fly: Fly) {
        var flyType = when (fly.flyType) {
            FlyType.PLANE_GCD -> {
                FlyType.BOOM_GCD_PLANE
            }
            FlyType.BULLET_GCD -> {
                FlyType.BOOM_GCD_BULLET

            }
            FlyType.PLANE_GMD -> {
                FlyType.BOOM_GMD_PLANE
            }
            FlyType.BULLET_GMD -> {
                FlyType.BOOM_GMD_BULLET
            }
            else -> {
                FlyType.BOOM
            }
        }

        val boom = FlyFactory.getBoom(activity, flyType)
        boom.x = (fly.cx - boom.w / 2)
        boom.y = (fly.cy - boom.h / 2)
        mapView.addFly(boom)

        val flyBoomView = boom.view as FlyBoomView
        //爆炸动画结束的时候将其从mapView中删除
        flyBoomView.animatorListener = object : FlyBoomView.AnimatorListener {
            override fun onAnimationEnd() {
                removeFly(boom)
            }
        }
    }


    /**
     * 添加敌机Fly
     */
    private fun addGmdPlane() {
        for (i in 0 until gmdPlaneCount) {
            createGmdPlane(FlyType.PLANE_GMD)
        }
    }


    /**
     * 发射子弹
     */
    private fun shot(plane: Plane) {
        val bullet = plane.shotBullet()!!
        addFly(bullet)
        moveFly(bullet)
    }


    /**
     * 移动Fly
     */
    private fun moveFly(fly: Fly) {
        var start = fly.cy

        var end = when (fly.flyType) {
            FlyType.BULLET_GCD -> {
                -fly.h * 2F
            }
            FlyType.PLANE_GCD -> {
                -fly.h * 2F
            }
            FlyType.PLANE_GMD -> {
                h + fly.h * 2F
            }
            FlyType.BULLET_GMD -> {
                h + fly.h * 2F

            }
            FlyType.BOSS -> {
                h + fly.h * 2F
            }
            FlyType.BULLET_BOSS -> {
                h + fly.h * 2F
            }
            else -> {
                -fly.h * 6F
            }
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
                        when (fly.flyType) {
                            FlyType.BULLET_GCD -> {
                                gcdBulletIsCollision(fly, gmdPlanes)
                            }
                            FlyType.BULLET_GMD -> {
                                gcdBulletIsCollision(fly, gcdPlanes)
                            }
                            FlyType.PLANE_GMD -> {
                                gcdBulletIsCollision(fly, gcdPlanes)
                            }
                        }

                    }
                }
                duration = (abs(start - end)).toLong() * fly.speed
                interpolator = LinearInterpolator()
                start()

            }
    }

    /**
     * Fly位置检测 如果已经超出屏幕则删除
     */
    private fun checkFlyPosition(fly: Fly): Boolean {
        //如果view已经不再屏幕内了 删除它
        if (fly.x + fly.w <= -fly.w * 2 ||

            fly.x >= w + fly.w * 2 ||

            fly.y + fly.h <= -fly.h * 2 ||

            fly.y >= h + fly.h * 2
        ) {
            removeFly(fly)
            return false
        }
        return true
    }

    /**
     * 我的子弹是否碰撞到敌机
     */
    private fun gcdBulletIsCollision(hitFly: Fly, beHitFlys: CopyOnWriteArrayList<Fly>) {
        for (beHitFly in beHitFlys) {
            //碰撞之后跳出循环
            if (isCollision(
                    hitFly.x,
                    hitFly.y,
                    hitFly.w.toFloat(),
                    hitFly.h.toFloat(),
                    beHitFly.x,
                    beHitFly.y,
                    beHitFly.w.toFloat(),
                    beHitFly.h.toFloat()
                )
            ) {
                sellingHP(hitFly, beHitFly)
                break
            }
        }
    }

    /**
     * 碰撞扣除HP
     */
    private fun sellingHP(fly1: Fly, fly2: Fly) {
        fly1.HP -= fly2.power
        fly2.HP -= fly1.power
        isDid(fly1)
        isDid(fly2)
    }

    /**
     * 飞行物是否死亡
     */
    private fun isDid(fly: Fly) {
        if (fly.HP <= 0) {
            createBoom(fly)
            removeFly(fly)
            if (fly.flyType == FlyType.PLANE_GCD) {
                isGameOver = true
                onGameOverListener?.onGameOver()
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
     * 手势拖动我的飞机
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun openFlyDrag(fly: Fly) {
        mapView.setOnTouchListener(object : View.OnTouchListener {
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
                        dx = mx
                        dy = my
                    }
                    MotionEvent.ACTION_UP -> {

                    }
                }
                return true
            }

            private fun move() {
                fly.x += (mx - dx)
                fly.y += (my - dy)

                //拖动超出屏幕检测
                if (fly.x < 0F) {
                    fly.x = 0F
                }
                if (fly.y < 0F) {
                    fly.y = 0F
                }

                if (fly.x + fly.w > w) {
                    fly.x = w.toFloat() - fly.w
                }
                if (fly.y + fly.h > h) {
                    fly.y = h.toFloat() - fly.h
                }
            }
        })
    }


    /**
     * 添加Fly到集合和mapView
     */
    private fun addFly(fly: Fly) {
        when (fly.flyType) {
            FlyType.BULLET_GCD -> {
                gcdBullets.add(fly)
            }
            FlyType.BULLET_GMD -> {
                gmdBullets.add(fly)
            }
            FlyType.PLANE_GCD -> {
                gcdPlanes.add(fly)
            }
            FlyType.PLANE_GMD -> {
                gmdPlanes.add(fly)
            }
            FlyType.BOSS -> {
                myLog("addBoss")
                gmdPlanes.add(fly)
            }

        }
        mapView.addFly(fly)
    }

    /**
     * 从集合和mapView中删除fly
     */
    private fun removeFly(fly: Fly) {
        when (fly.flyType) {
            FlyType.BULLET_GCD -> {
                gcdBullets.remove(fly)
            }
            FlyType.BULLET_GMD -> {
                gcdBullets.remove(fly)
            }
            FlyType.PLANE_GMD -> {
                createGmdPlane(FlyType.PLANE_GMD)
                gmdPlanes.remove(fly)
            }
            FlyType.BOSS -> {
                timerBoss()
                gmdPlanes.remove(fly)
            }
            FlyType.PLANE_GCD -> {
                gcdPlanes.remove(fly)
            }
        }
        mapView.removeFly(fly)
        fly.boom()
    }

    fun quitGame() {
        isQuitGame = true
    }

    interface OnGameOverListener {
        fun onGameOver()
    }
}