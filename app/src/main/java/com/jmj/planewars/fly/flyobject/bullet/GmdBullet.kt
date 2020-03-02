package com.jmj.planewars.fly.flyobject.bullet

import android.view.View
import com.jmj.planewars.fly.cons.FlyType

class GmdBullet : Bullet {
    constructor(
        view: View,
        flyType: FlyType,
        w: Int,
        h: Int,
        speed: Int,
        power: Int,
        HP: Int
    ) : super(view, flyType, w, h, speed, power, HP)
}