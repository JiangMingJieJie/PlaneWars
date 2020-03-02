package com.jmj.planewars.fly.flyobject

import android.view.View
import com.jmj.planewars.fly.cons.FlyType

class Plane : Fly {


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