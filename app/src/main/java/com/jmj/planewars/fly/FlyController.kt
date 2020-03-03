package com.jmj.planewars.fly

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyfactory.FlyFactory
import com.jmj.planewars.fly.flyobject.Fly
import com.jmj.planewars.fly.flyobject.Plane
import com.jmj.planewars.fly.view.FlyBoomView
import com.jmj.planewars.fly.view.MapView
import com.jmj.planewars.fly.view.PlaneView
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

    var onGameOverListener: OnGameProgressListener? = null


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
                startBossTimer()
                startGmdPlaneShotThread()
            }
        }
    }

    /**
     * 重新开始游戏
     */
    fun reStart() {
        isGameOver = false
        addGcdPlane()
    }


    /**
     * 我的飞机自动发射子弹线程
     */
    private fun startGcdPlaneShotThread() {
        Thread {
            while (!isGameOver && !isQuitGame) {
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
                        if (random.nextInt(3) % 3 == 0 || it.flyType == FlyType.PLANE_BOSS)
                            shot(it as Plane)
                    }
                }
                SystemClock.sleep(2000)

            }
        }.start()
    }

    /**
     * 定时刷boss
     */
    private fun startBossTimer() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                activity.runOnUiThread {
                    createPlane(FlyType.PLANE_BOSS)
                }
            }
        }, (random.nextInt(3) + 1) * 2000.toLong())
    }


    /**
     * 添加我的飞机
     */
    fun addGcdPlane() {
        createPlane(FlyType.PLANE_GCD)
        startGcdPlaneShotThread()
    }

    /**
     * 添加敌机
     */
    private fun addGmdPlane() {
        for (i in 0 until gmdPlaneCount) {
            createPlane(FlyType.PLANE_GMD)
        }
    }


    /**
     * 创建飞机并添加到集合
     */
    private fun createPlane(flyType: FlyType) {
        val plane = FlyFactory.getPlane(activity, flyType)!!

        if (flyType == FlyType.PLANE_GCD) {
            //我的飞机的位置
            plane.x = w / 2F - plane.w / 2
            plane.y = h - plane.h.toFloat()
            (plane.view as PlaneView).openMask()
            openFlyDrag(plane)
        } else if (flyType == FlyType.PLANE_GMD) {
            //敌机的位置
            plane.x = (w - plane.w) * random.nextFloat()
            plane.y = -plane.h * 2F
        } else {
            //boss机的位置
            plane.x = (w - plane.w) * random.nextFloat()
            plane.y = -plane.h * 2F
            moveFlyX(plane)
        }

        addFly(plane)

        moveFlyY(plane)
    }


    /**
     * 创建爆炸效果
     */
    private fun createBoom(fly: Fly) {
        val boom = FlyFactory.getBoom(activity, fly.flyType)!!
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
     * 创建子弹
     */
    private fun createBullet(plane: Plane) {
        val bullet = FlyFactory.getBullet(activity, plane.flyType)!!

        bullet.x = (plane.cx - bullet.w / 2)

        bullet.y = (plane.cy - bullet.h / 2)

        addFly(bullet)

        moveFlyY(bullet)
    }

    /**
     * 发射子弹
     */
    private fun shot(plane: Plane) {
        createBullet(plane)
    }


    /**
     * y轴移动Fly
     */
    private fun moveFlyY(fly: Fly) {
        //如果fly已经死亡
        if (fly.isBoom) {
            return
        }

        var start = fly.cy

        var end = when (fly.flyType) {
            FlyType.BULLET_GCD -> {
                -fly.h * 2
            }
            FlyType.PLANE_GCD -> {
                return
            }
            FlyType.PLANE_GMD -> {
                h + fly.h * 2
            }
            FlyType.BULLET_GMD -> {
                h + fly.h * 2
            }
            FlyType.PLANE_BOSS -> {
                0
            }
            else -> {
                h + fly.h * 2
            }
        }


        ValueAnimator.ofFloat(start, end.toFloat())
            .apply {
                addUpdateListener {
                    //如果fly已经死亡
                    if (fly.isBoom) {
                        cancel()
                        return@addUpdateListener
                    }

                    //移动Fly
                    fly.y = (it.animatedValue as Float)

                    //超出屏幕检测
                    if (checkFlyPosition(fly)) {
                        selectHitAndBeHit(fly)
                    }
                }
                duration = (abs(start - end)).toLong() * fly.speed
                interpolator = LinearInterpolator()
                start()
            }

    }

    /**
     * X轴移动Fly
     */
    private fun moveFlyX(fly: Fly) {
        //如果fly已经死亡
        if (fly.isBoom) {
            return
        }

        var start = fly.x
        var end = if (fly.x < w - fly.w) {
            w.toFloat() - fly.w
        } else {
            0F
        }
        ValueAnimator.ofFloat(start, end).apply {
            addUpdateListener {
                //如果fly已经死亡
                if (fly.isBoom) {
                    cancel()
                    return@addUpdateListener
                }
                fly.x = it.animatedValue as Float
                //超出屏幕检测
                if (checkFlyPosition(fly)) {
                    selectHitAndBeHit(fly)
                }
            }
            duration = (abs(start - end)).toLong() * fly.speed
            interpolator = LinearInterpolator()
            doOnEnd {
                moveFlyX(fly)
            }
            start()
        }
    }


    /**
     * Fly位置检测 如果已经超出屏幕则删除
     */
    private fun checkFlyPosition(fly: Fly): Boolean {
        if (fly.flyType == FlyType.PLANE_BOSS) return true
        //如果view已经不再屏幕内了 删除它
        if (fly.x + fly.w <= -fly.w ||

            fly.x >= w + fly.w ||

            fly.y + fly.h <= -fly.h ||

            fly.y >= h + fly.h
        ) {
            removeFly(fly)
            return false
        }
        return true
    }

    /**
     * 通过flyType来决定选择哪个集合中的元素与它进行碰撞检测
     */
    private fun selectHitAndBeHit(fly: Fly) {
        when (fly.flyType) {
            FlyType.BULLET_GCD -> {
                //如果是我的子弹，则与敌机集合检测
                hitAndBeHit(fly, gmdPlanes)
            }
            FlyType.BULLET_GMD -> {
                //如果是敌机的子弹则与我机检测
                hitAndBeHit(fly, gcdPlanes)
            }
            FlyType.PLANE_GMD -> {
                //...
                hitAndBeHit(fly, gcdPlanes)
            }
            FlyType.PLANE_BOSS -> {
                //...
                hitAndBeHit(fly, gcdPlanes)
            }
            FlyType.BULLET_BOSS -> {
                //...
                hitAndBeHit(fly, gcdPlanes)
            }
        }
    }

    /**
     * 碰撞和被碰撞检测
     */
    private fun hitAndBeHit(hitFly: Fly, beHitFlys: CopyOnWriteArrayList<Fly>) {
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
            if (fly.flyType == FlyType.PLANE_GMD || fly.flyType == FlyType.PLANE_BOSS) {
                onGameOverListener?.onKill(fly.flyType)
            }
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
            FlyType.BULLET_BOSS -> {
                gmdBullets.add(fly)
            }
            FlyType.PLANE_BOSS -> {
                gmdPlanes.add(fly)
            }
            FlyType.PLANE_GCD -> {
                gcdPlanes.add(fly)
            }
            FlyType.PLANE_GMD -> {
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
                gmdBullets.remove(fly)
            }
            FlyType.PLANE_GMD -> {
                createPlane(FlyType.PLANE_GMD)
                gmdPlanes.remove(fly)
            }
            FlyType.PLANE_GCD -> {
                gcdPlanes.remove(fly)
            }
            FlyType.BULLET_BOSS -> {
                gmdBullets.remove(fly)
            }
            FlyType.PLANE_BOSS -> {
                startBossTimer()
                gmdPlanes.remove(fly)
            }
        }
        mapView.removeFly(fly)
        fly.boom()
    }

    fun quitGame() {
        isQuitGame = true
    }

    interface OnGameProgressListener {
        fun onGameOver() {}
        fun onKill(flyType: FlyType) {}
        fun onStart() {}
        fun onPause() {}
    }
}