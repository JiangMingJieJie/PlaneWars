package com.jmj.planewars.fly.flyobject.plane

import android.view.View
import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyfactory.FlyFactory
import com.jmj.planewars.fly.flyobject.bullet.Bullet

/**
 * 共产主义飞机
 */
class GcdPlane : Plane {
    constructor(
        view: View,
        flyType: FlyType,
        w: Int,
        h: Int,
        speed: Int,
        power: Int,
        hp: Int
    ) : super(view, flyType, w, h, speed, power, hp)


    override fun shotBullet(): Bullet?{
        val bullet = FlyFactory.getBullet(context, FlyType.BULLET_GCD)
        bullet.x = (cx - bullet.w/2)
        bullet.y = (cy - bullet.h/2)
        return bullet
    }



}