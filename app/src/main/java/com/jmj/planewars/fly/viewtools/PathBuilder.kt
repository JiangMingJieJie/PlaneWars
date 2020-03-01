package com.jmj.planewars.fly.viewtools

import android.graphics.Path


class PathBuilder {

    var path: Path? = null

    fun append(x: Float, y: Float): PathBuilder {

        if (path == null) {
            path = Path()
            path!!.moveTo(x, y)
        } else {
            path!!.lineTo(x, y)
        }
        return this
    }


    fun build(): Path {
        return path!!
    }
}