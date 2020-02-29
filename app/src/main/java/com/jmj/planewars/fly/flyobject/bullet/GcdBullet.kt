package com.jmj.planewars.fly.flyobject.bullet

import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.view.FlyView
/**
 * 共产主义子弹
 */
class GcdBullet : Bullet {
    constructor(flyView: FlyView, flyType: FlyType, w: Int, h: Int, speed: Int, power: Int) : super(
        flyView,
        flyType,
        w,
        h,
        speed,
        power
    )
}