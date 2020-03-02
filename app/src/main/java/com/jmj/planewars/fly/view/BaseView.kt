package com.jmj.planewars.fly.view

import android.content.Context
import android.util.AttributeSet
import android.view.View

open class BaseView :View{
    var w = 0F
    var h = 0F
    var cx = 0F
    var cy = 0F

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        w = measuredWidth.toFloat()
        h = measuredHeight.toFloat()
        cx = w / 2
        cy = h / 2
    }
}