package com.jmj.planewars.fly.flyobject

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.jmj.planewars.fly.cons.FlyType

abstract class Fly {
    /**
     * 飞行器展示的view
     */
    var view: View
    /**
     * 这个飞行器是否已经死亡
     */
    var isBoom = false
    /**
     * 飞行器的移动速度 越小越快
     */
    var speed = 1
    /**
     * 飞行器类型
     */
    var flyType: FlyType
    /**
     * 飞行器的宽高
     */
    var w = 0
    var h = 0
    /**
     * 上下文
     */
    var context: Context
    /**
     * fly的中心点
     */
    var cx = 0F
    var cy = 0F
    /**
     * fly的偏移值 xy
     */
    var x = 0F
        set(value) {
            view.x = value
            cx = (view.x + w / 2)
            field = value
        }
    var y = 0F
        set(value) {
            view.y = value
            cy = (view.y + h / 2)
            field = value
        }

    var power = 100

    var HP = 100

    constructor(view: View, flyType: FlyType, w: Int, h: Int, speed: Int, power: Int, HP: Int) {
        this.view = view
        this.flyType = flyType
        this.speed = speed
        this.w = w
        this.h = h
        this.power = power
        this.HP = HP
        this.context = view.context
        val layoutParams = ViewGroup.LayoutParams(w, h)
        view.layoutParams = layoutParams
    }

    fun setFlyPosition(x: Int, y: Int) {
    }

    /**
     * 死亡
     */
    fun boom() {
        isBoom = true
    }


}