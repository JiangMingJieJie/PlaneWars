package com.jmj.planewars.fly.flyfactory

import android.content.Context
import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyobject.bullet.Bullet
import com.jmj.planewars.fly.flyobject.bullet.GcdBullet
import com.jmj.planewars.fly.flyobject.bullet.GmdBullet
import com.jmj.planewars.fly.flyobject.plane.GcdPlane
import com.jmj.planewars.fly.flyobject.plane.GmdPlane
import com.jmj.planewars.fly.flyobject.plane.Plane
import com.jmj.planewars.fly.view.FlyView
import com.jmj.planewars.fly.view.PlaneGcdView
import com.jmj.planewars.fly.view.PlaneGmdView
import com.jmj.planewars.tools.dp2px
import java.util.*

object FlyFactory {
    private var random = Random()

    /**
     * 获取飞机
     */
    fun getPlane(context: Context, flyType: FlyType): Plane {
        return when (flyType) {
            FlyType.PLANE_GCD -> {
                GcdPlane(
                    getFlyView(context, flyType),
                    flyType,
                    dp2px(50),
                    dp2px(100),
                    1,
                    100
                )
            }
            FlyType.PLANE_GMD -> {
                GmdPlane(
                    getFlyView(context, flyType),
                    flyType,
                    dp2px(50),
                    dp2px(100),
                    random.nextInt(10) + 1,
                    100
                )
            }
            else -> {
                GmdPlane(
                    getFlyView(context, flyType),
                    flyType,
                    dp2px(50),
                    dp2px(100),
                    5,
                    100
                )
            }
        }
    }

    /**
     * 获取子弹
     */
    fun getBullet(context: Context, flyType: FlyType): Bullet {
        return when (flyType) {
            FlyType.BULLET_GCD -> {
                GcdBullet(
                    getFlyView(context, flyType),
                    flyType,
                    dp2px(15),
                    dp2px(30),
                    3,
                    100
                )
            }
            FlyType.BULLET_GMD -> {
                GmdBullet(
                    getFlyView(context, flyType),
                    flyType,
                    dp2px(15),
                    dp2px(30),
                    1,
                    100
                )
            }
            else -> {
                GmdBullet(
                    getFlyView(context, flyType),
                    flyType,
                    dp2px(15),
                    dp2px(30),
                    1,
                    100
                )
            }
        }
    }

    /**
     * 获取飞机或子弹的视图
     */
    private fun getFlyView(context: Context, flyType: FlyType): FlyView {
        var flyView: FlyView? = null
        when (flyType) {
            FlyType.PLANE_GCD -> {
                flyView = PlaneGcdView(context)
            }
            FlyType.PLANE_GMD -> {
                flyView = PlaneGmdView(context)
                flyView.isReverse = true
            }
            FlyType.BULLET_GCD -> {
                flyView = PlaneGcdView(context)
            }
            FlyType.BULLET_GMD -> {
                flyView = PlaneGmdView(context)
                flyView.isReverse = true
            }
        }
        return flyView!!
    }

}