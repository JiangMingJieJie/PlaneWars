package com.jmj.planewars.fly.flyfactory

import android.content.Context
import android.graphics.Color
import android.view.View
import com.jmj.planewars.fly.cons.FlyColors
import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyobject.Boom
import com.jmj.planewars.fly.flyobject.bullet.Bullet
import com.jmj.planewars.fly.flyobject.bullet.GcdBullet
import com.jmj.planewars.fly.flyobject.bullet.GmdBullet
import com.jmj.planewars.fly.flyobject.plane.GcdPlane
import com.jmj.planewars.fly.flyobject.plane.GmdPlane
import com.jmj.planewars.fly.flyobject.plane.Plane
import com.jmj.planewars.fly.view.*
import com.jmj.planewars.tools.dp2px
import java.util.*

object FlyFactory {
    private var random = Random()
    /**
     * 我的飞机大小
     */
    private var gcdPlaneLength = 40
    /**
     * 敌机大小
     */
    private var gmdPlaneLength = 30

    private var bossLength = 100
    /**
     * 我的飞机子弹大小
     */
    private var gcdBulletLength = 20
    /**
     * 敌机子弹大小
     */
    private var gmdBulletLength = 20
    /**
     * 我的飞机HP
     */
    private var gcdPlaneHP = 100000
    /**
     * 我的飞机碰撞威力
     */
    private var gcdPlanePower = 100
    /**
     * 敌机HP
     */
    private var gmdPlaneHp = 100

    private var bossHp = 3000
    /**
     * 敌机碰撞威力
     */
    private var gmdPlanePower = 100
    private var bossPower = 1000
    /**
     * 我的子弹HP
     */
    private var gcdBulletHP = 0
    /**
     * 我的飞机子弹威力
     */
    private var gcdBulletPower = 100
    /**
     * 敌机子弹HP
     */
    private var gmdBulletHP = 0
    /**
     * 敌机子弹威力
     */
    private var gmdBulletPower = 100


    /**
     * 获取飞机
     */
    fun getPlane(context: Context, flyType: FlyType): Plane {
        return when (flyType) {
            FlyType.PLANE_GCD -> {
                GcdPlane(
                    getView(context, flyType),
                    flyType,
                    dp2px(gcdPlaneLength),
                    dp2px(gcdPlaneLength),
                    1,
                    gcdPlanePower,
                    gcdPlaneHP
                )
            }
            FlyType.PLANE_GMD -> {
                GmdPlane(
                    getView(context, flyType),
                    flyType,
                    dp2px(gmdPlaneLength),
                    dp2px(gmdPlaneLength),
                    random.nextInt(3) + 3,
                    gmdPlanePower,
                    gmdPlaneHp
                )
            }
            FlyType.BOSS -> {
                GmdPlane(
                    getView(context, flyType),
                    flyType,
                    dp2px(bossLength),
                    dp2px(bossLength),
                    20,
                    bossPower,
                    bossHp
                )
            }
            else -> {
                GmdPlane(
                    getView(context, flyType),
                    flyType,
                    dp2px(gmdPlaneLength),
                    dp2px(gmdPlaneLength),
                    random.nextInt(3) + 3,
                    gmdPlanePower,
                    gmdPlaneHp
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
                    getView(context, flyType),
                    flyType,
                    dp2px(gcdBulletLength),
                    dp2px(gcdBulletLength),
                    1,
                    gcdBulletPower,
                    gcdBulletHP
                )
            }
            FlyType.BULLET_GMD -> {
                GmdBullet(
                    getView(context, flyType),
                    flyType,
                    dp2px(gmdBulletLength),
                    dp2px(gmdBulletLength),
                    random.nextInt(3),
                    gmdBulletPower,
                    gmdBulletHP
                )
            }
            FlyType.BULLET_BOSS -> {
                GmdBullet(
                    getView(context, flyType),
                    flyType,
                    dp2px(40),
                    dp2px(40),
                    1,
                    gmdBulletPower,
                    gmdBulletHP
                )
            }
            else -> {
                GmdBullet(
                    getView(context, flyType),
                    flyType,
                    dp2px(gmdBulletLength),
                    dp2px(gmdBulletLength),
                    random.nextInt(3),
                    gmdBulletPower,
                    gmdBulletHP
                )
            }
        }
    }

    /**
     * 获取爆炸效果的Fly
     */
    fun getBoom(context: Context, flyType: FlyType): Boom {
        return when (flyType) {
            FlyType.BOOM_GMD_PLANE -> {
                Boom(
                    getBoomView(context, flyType),
                    flyType,
                    dp2px(gmdPlaneLength),
                    dp2px(gmdPlaneLength),
                    0
                )
            }
            FlyType.BOOM_GMD_BULLET -> {
                Boom(
                    getBoomView(context, flyType),
                    flyType,
                    dp2px(gmdBulletLength),
                    dp2px(gmdBulletLength),
                    0
                )
            }

            FlyType.BOOM_GCD_PLANE -> {
                Boom(
                    getBoomView(context, flyType),
                    flyType,
                    dp2px(gcdPlaneLength),
                    dp2px(gcdBulletLength),
                    0
                )
            }

            FlyType.BOOM_GCD_BULLET -> {
                Boom(
                    getBoomView(context, flyType),
                    flyType,
                    dp2px(gmdBulletLength),
                    dp2px(gmdBulletLength),
                    0
                )
            }

            else -> {
                Boom(
                    getBoomView(context, flyType),
                    flyType,
                    dp2px(gmdPlaneLength),
                    dp2px(gmdPlaneLength),
                    0
                )
            }

        }
    }

    /**
     * 获取爆炸效果的View
     */
    private fun getBoomView(context: Context, flyType: FlyType): View {
        var view: View
        when (flyType) {
            FlyType.BOOM_GMD_PLANE -> {
                view = FlyBoomView(context, FlyColors.GMD_PLANE_WING)
            }
            FlyType.BOOM_GMD_BULLET -> {
                view = FlyBoomView(context, FlyColors.GMD_BULLET)
            }

            FlyType.BOOM_GCD_PLANE -> {
                view = FlyBoomView(context, FlyColors.GCD_PLANE_WING)
            }

            FlyType.BOOM_GCD_BULLET -> {
                view = FlyBoomView(context, FlyColors.GCD_BULLET)
            }
            else -> {
                view = FlyBoomView(context)
            }

        }
        return view
    }

    /**
     * 获取飞机或子弹的View
     */
    private fun getView(context: Context, flyType: FlyType): View {
        var view: View? = null
        when (flyType) {
            FlyType.PLANE_GCD -> {
                view = PlaneGcdView(context)
            }
            FlyType.PLANE_GMD -> {
                view = PlaneGmdView(context)
                view.isReverse = true
            }
            FlyType.BULLET_GCD -> {
                view = BulletGcdView(context)
            }
            FlyType.BULLET_GMD -> {
                view = BulletGmdView(context)
                view.isReverse = true
            }
            FlyType.BOSS -> {
                view = PlaneGmdView(context)
                view.isReverse = true
            }
            FlyType.BULLET_BOSS -> {
                view = BulletGmdView(context)
                view.isReverse = true
            }
        }
        return view!!
    }

}