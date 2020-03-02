package com.jmj.planewars.fly.flyobject

import android.view.View
import com.jmj.planewars.fly.cons.FlyType
import com.jmj.planewars.fly.flyobject.Fly


class Bullet : Fly {

    var bulletColor = 0

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