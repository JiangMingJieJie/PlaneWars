package com.jmj.planewars.fly.flyobject.plane

import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyfactory.FlyFactory
import com.jmj.planewars.fly.flyobject.bullet.Bullet
import com.jmj.planewars.fly.view.FlyView

class GmdPlane : Plane {
    constructor(flyView: FlyView, flyType: FlyType, w: Int, h: Int, speed: Int, hp: Int) : super(
        flyView,
        flyType,
        w,
        h,
        speed,
        hp
    )


    override fun shotBullet(): Bullet? {
        val bullet = FlyFactory.getBullet(context, FlyType.BULLET_GMD)
        bullet.x = (cx - bullet.w / 2)
        bullet.y = (cy - bullet.h / 2)
        return bullet
    }


}