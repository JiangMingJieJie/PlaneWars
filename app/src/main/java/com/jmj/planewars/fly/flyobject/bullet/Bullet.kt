package com.jmj.planewars.fly.flyobject.bullet

import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyobject.Fly
import com.jmj.planewars.fly.view.FlyView

abstract class Bullet : Fly {
    var power = 0

    constructor(flyView: FlyView, flyType: FlyType, w: Int, h: Int, speed: Int, power: Int) : super(
        flyView,
        flyType,
        w,
        h,
        speed
    ) {
        this.power = power
    }
}