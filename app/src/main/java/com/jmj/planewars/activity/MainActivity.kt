package com.jmj.planewars.activity

import android.content.DialogInterface
import android.os.Bundle
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
        flyController = FlyController(this, mapView)

        flyController.onGameOverListener = object : FlyController.OnGameOverListener {
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

}
