package com.jmj.planewars.fly.flyobject

import android.view.View
import com.jmj.planewars.fly.cons.FlyType

class Boom : Fly {
    constructor(view: View, flyType: FlyType, w: Int, h: Int, speed: Int) : super(
        view,
        flyType,
        w,
        h,
        speed,
        0,
        0
    )
}