package com.jmj.planewars.fly.flyfactory

import android.content.Context
import android.graphics.Color
import android.view.View
import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyobject.Boom
import com.jmj.planewars.fly.flyobject.Bullet
import com.jmj.planewars.fly.flyobject.Plane
import com.jmj.planewars.fly.view.*
import com.jmj.planewars.tools.dp2px
import java.util.*

object FlyFactory {
    private var random = Random()
    /**
     * 我的飞机
     */
    private var gcdPlaneLength = 40  //飞机大小
    private var gcdBulletLength = 20 //子弹大小
    private var gcdPlaneHP = 10000  //飞机生命值
    private var gcdPlanePower = 100  //飞机撞击威力
    private var gcdBulletHP = 0      //子弹生命值
    private var gcdBulletPower = 100  //子弹威力

    /**
     * 敌机
     */
    private var gmdPlaneLength = 30    //飞机大小
    private var gmdBulletLength = 20   //子弹大小
    private var gmdPlaneHp = 100     //飞机生命值
    private var gmdPlanePower = 100    //飞机撞击威力
    private var gmdBulletHP = 0        //子弹生命值
    private var gmdBulletPower = 100   //子弹威力

    /**
     * BOSS
     */
    private var bossPlaneLength = 150    //飞机大小
    private var bossBulletLength = 60   //子弹大小
    private var bossPlaneHp = 5000       //飞机生命值
    private var bossPlanePower = 100    //飞机撞击威力
    private var bossBulletHP = 0        //子弹生命值
    private var bossBulletPower = 100   //子弹威力


    private var GMD_PLANE_WING = Color.parseColor("#42AFF5")
    private var GMD_PLANE_BODY = Color.parseColor("#F06292")
    private var GMD_BULLET = GMD_PLANE_BODY

    private var GCD_PLANE_WING = Color.parseColor("#FFC107")
    private var GCD_PLANE_BODY = Color.parseColor("#009688")
    private var GCD_BULLET = GCD_PLANE_WING

    private var BOSS_PLANE_WING = Color.parseColor("#FF5722")
    private var BOSS_PLANE_BODY = Color.parseColor("#00BCD4")
    private var BOSS_BULLET = BOSS_PLANE_WING

    /**
     * 获取飞机的Fly
     */
    fun getPlane(context: Context, flyType: FlyType): Plane? {
        return when (flyType) {
            FlyType.PLANE_GCD -> {
                Plane(
                    getView(context, flyType)!!,
                    flyType,
                    dp2px(gcdPlaneLength),
                    dp2px(gcdPlaneLength),
                    1,
                    gcdPlanePower,
                    gcdPlaneHP
                )
            }
            FlyType.PLANE_GMD -> {
                Plane(
                    getView(context, flyType)!!,
                    flyType,
                    dp2px(gmdPlaneLength),
                    dp2px(gmdPlaneLength),
                    random.nextInt(3) + 3,
                    gmdPlanePower,
                    gmdPlaneHp
                )
            }
            FlyType.PLANE_BOSS -> {
                Plane(
                    getView(context, flyType)!!,
                    flyType,
                    dp2px(bossPlaneLength),
                    dp2px(bossPlaneLength),
                    2,
                    bossPlanePower,
                    bossPlaneHp
                )
            }
            else -> null
        }
    }

    /**
     * 获取子弹的Fly
     */
    fun getBullet(context: Context, flyType: FlyType): Bullet? {
        return when (flyType) {
            FlyType.PLANE_GCD -> {
                Bullet(
                    getView(context, FlyType.BULLET_GCD)!!,
                    FlyType.BULLET_GCD,
                    dp2px(gcdBulletLength),
                    dp2px(gcdBulletLength),
                    1,
                    gcdBulletPower,
                    gcdBulletHP
                )
            }
            FlyType.PLANE_GMD -> {
                Bullet(
                    getView(context, FlyType.BULLET_GMD)!!,
                    FlyType.BULLET_GMD,
                    dp2px(gmdBulletLength),
                    dp2px(gmdBulletLength),
                    random.nextInt(3),
                    gmdBulletPower,
                    gmdBulletHP
                )
            }
            FlyType.PLANE_BOSS -> {
                Bullet(
                    getView(context, FlyType.BULLET_BOSS)!!,
                    FlyType.BULLET_BOSS,
                    dp2px(bossBulletLength),
                    dp2px(bossBulletLength),
                    1,
                    bossBulletPower,
                    bossBulletHP
                )
            }
            else -> {
                null
            }
        }
    }

    /**
     * 获取爆炸效果的Fly
     */
    fun getBoom(context: Context, flyType: FlyType): Boom? {
        return when (flyType) {
            FlyType.PLANE_GMD -> {
                Boom(
                    getBoomView(context, flyType)!!,
                    FlyType.BOOM,
                    dp2px(gmdPlaneLength),
                    dp2px(gmdPlaneLength),
                    0
                )
            }
            FlyType.BULLET_GMD -> {
                Boom(
                    getBoomView(context, flyType)!!,
                    FlyType.BOOM,
                    dp2px(gmdBulletLength),
                    dp2px(gmdBulletLength),
                    0
                )
            }

            FlyType.PLANE_GCD -> {
                Boom(
                    getBoomView(context, flyType)!!,
                    FlyType.BOOM,
                    dp2px(gcdPlaneLength),
                    dp2px(gcdBulletLength),
                    0
                )
            }


            FlyType.BULLET_GCD -> {
                Boom(
                    getBoomView(context, flyType)!!,
                    FlyType.BOOM,
                    dp2px(gmdBulletLength),
                    dp2px(gmdBulletLength),
                    0
                )
            }

            FlyType.PLANE_BOSS -> {
                Boom(
                    getBoomView(context, flyType)!!,
                    FlyType.BOOM,
                    dp2px(bossPlaneLength),
                    dp2px(bossBulletLength),
                    0
                )
            }

            FlyType.BULLET_BOSS -> {
                Boom(
                    getBoomView(context, flyType)!!,
                    FlyType.BOOM,
                    dp2px(bossBulletLength),
                    dp2px(bossBulletLength),
                    0
                )
            }

            else -> {
                null
            }

        }
    }

    /**
     * 获取飞机或子弹的View
     */
    private fun getBoomView(context: Context, flyType: FlyType): View? {
        return when (flyType) {
            FlyType.PLANE_GMD -> {
                FlyBoomView(context, GMD_PLANE_WING)
            }
            FlyType.BULLET_GMD -> {
                FlyBoomView(context, GMD_BULLET)
            }
            FlyType.PLANE_GCD -> {
                FlyBoomView(context, GCD_PLANE_WING)
            }
            FlyType.BULLET_GCD -> {
                FlyBoomView(context, GCD_BULLET)
            }
            FlyType.PLANE_BOSS -> {
                FlyBoomView(context, BOSS_PLANE_WING)
            }
            FlyType.BULLET_BOSS -> {
                FlyBoomView(context, BOSS_BULLET)
            }
            else -> null
        }
    }

    /**
     * 获取飞机或子弹的View
     */
    private fun getView(context: Context, flyType: FlyType): View? {
        return when (flyType) {
            FlyType.PLANE_GCD -> {
                PlaneView(context, GCD_PLANE_WING, GCD_PLANE_BODY, false)
            }
            FlyType.PLANE_BOSS -> {
                PlaneView(context, BOSS_PLANE_WING, BOSS_PLANE_BODY, true)
            }
            FlyType.PLANE_GMD -> {
                PlaneView(context, GMD_PLANE_WING, GMD_PLANE_BODY, true)
            }
            FlyType.BULLET_GCD -> {
                BulletView(context, GCD_BULLET, false)
            }
            FlyType.BULLET_GMD -> {
                BulletView(context, GMD_BULLET, true)
            }
            FlyType.BULLET_BOSS -> {
                BulletView(context, BOSS_BULLET, true)
            }
            else -> null
        }
    }

}