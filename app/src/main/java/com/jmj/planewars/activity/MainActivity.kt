package com.jmj.planewars.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.jmj.planewars.R
import com.jmj.planewars.fly.FlyController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flyController = FlyController(this, mapView)

        btn.isVisible = false
        btn.setOnClickListener {
        }
    }
}
