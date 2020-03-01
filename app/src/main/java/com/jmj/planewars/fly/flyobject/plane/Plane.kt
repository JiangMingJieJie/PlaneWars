package com.jmj.planewars.fly.flyobject.plane

import android.view.View
import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyobject.Fly
import com.jmj.planewars.fly.flyobject.bullet.Bullet
import com.jmj.planewars.fly.view.FlyView


abstract class Plane : Fly {
    constructor(
        view: View,
        flyType: FlyType,
        w: Int,
        h: Int,
        speed: Int,
        power: Int,
        HP: Int
    ) : super(view, flyType, w, h, speed, power, HP)


    /**
     * 发射子弹
     */
    abstract fun shotBullet(): Bullet?





}