package com.jmj.planewars.fly.flyobject.plane

import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyobject.Fly
import com.jmj.planewars.fly.flyobject.bullet.Bullet
import com.jmj.planewars.fly.view.FlyView


abstract class Plane : Fly {
    var hp = 0

    constructor(flyView: FlyView, flyType: FlyType, w: Int, h: Int, speed: Int, hp: Int) : super(
        flyView,
        flyType,
        w,
        h,
        speed
    ) {
        this.hp = hp
    }


    /**
     * 发射子弹
     */
    abstract fun shotBullet(): Bullet?





}