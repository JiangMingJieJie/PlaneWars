package com.jmj.planewars.fly.flyobject.plane

import android.view.View
import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyfactory.FlyFactory
import com.jmj.planewars.fly.flyobject.Fly
import com.jmj.planewars.fly.flyobject.bullet.Bullet

class GmdPlane : Plane {
    constructor(
        view: View,
        flyType: FlyType,
        w: Int,
        h: Int,
        speed: Int,
        power: Int,
        hp: Int
    ) : super(view, flyType, w, h, speed, power, hp)


    override fun shotBullet(): Bullet? {
        var flyType = if (flyType == FlyType.BOSS) FlyType.BULLET_BOSS else FlyType.BULLET_GMD
        val bullet = FlyFactory.getBullet(context, flyType)
        bullet.x = (cx - bullet.w / 2)
        bullet.y = (cy - bullet.h / 2)
        return bullet
    }


}