package com.jmj.planewars.activity

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jmj.planewars.R
import com.jmj.planewars.fly.FlyController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var flyController: FlyController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fullScreen()
        flyController = FlyController(this, mapView)

        flyController.onGameOverListener = object : FlyController.OnGameProgressListener {
            override fun onGameOver() {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("游戏结束!点击确定继续游戏,点击取消退出游戏")
                    .setNegativeButton("取消") { _: DialogInterface, _: Int ->
                        finish()
                    }
                    .setPositiveButton("确定") { _: DialogInterface, _: Int ->
                        flyController.reStart()
                    }.show()
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        flyController.quitGame()
    }

    override fun onBackPressed() {

    }

    private fun fullScreen() {
        window.decorView.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

}
