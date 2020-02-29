package com.jmj.planewars.fly.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.jmj.planewars.fly.flyobject.Fly
import com.jmj.planewars.tools.myLog

/**
 * 飞行物的容器
 */
class MapView : ViewGroup {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var w = 0
    private var h = 0
    private var loadFinish = false
    var onMeasureFinishListener: OnViewLoadFinishListener? = null

    init {
        setWillNotDraw(true)
    }

    /**
     * 指定飞行物的位置
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child != null) {
                val childW = child.measuredWidth
                val childH = child.measuredHeight
                child.layout(0, 0, childW, childH)
            }

        }
    }

    /**
     * 测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        w = measuredWidth
        h = measuredHeight
        if (!loadFinish) {
            onMeasureFinishListener?.onFinish(w, h)
            loadFinish = true
        }
    }


    /**
     * 添加飞行物
     */
    fun addFly(fly: Fly) {
        addView(fly.flyView)

    }

    /**
     * 删除飞行物
     */
    fun removeFly(fly: Fly) {
        removeView(fly.flyView)
    }


    interface OnViewLoadFinishListener {
        fun onFinish(w: Int, h: Int)
    }

}