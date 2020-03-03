package com.jmj.planewars.activity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import com.jmj.planewars.R
import com.jmj.planewars.fly.FlyController
import com.jmj.planewars.fly.cons.FlyType
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var flyController: FlyController
    private var killAnim2One: ValueAnimator? = null
    private var killAnim2Zero: ValueAnimator? = null
    private var lastAnimViewId = 0
    private var killCount = 0
    private var killBossCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fullScreen()
        flyController = FlyController(this, mapView)

        flyController.onGameOverListener = object : FlyController.OnGameProgressListener {
            override fun onGameOver() {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("游戏结束! 点击确定继续游戏,点击取消退出游戏")
                    .setNegativeButton("取消") { _: DialogInterface, _: Int ->
                        finish()
                    }.setCancelable(false)
                    .setPositiveButton("确定") { _: DialogInterface, _: Int ->
                        flyController.reStart()
                    }.show()
            }

            override fun onStart() {
                super.onStart()
                killCount = 0
                killBossCount = 0
            }

            @SuppressLint("SetTextI18n")
            override fun onKill(flyType: FlyType) {
                super.onKill(flyType)
                if (flyType == FlyType.PLANE_GMD) {
                    killCount++
                    tv_kill_count.text = "击败 $killCount"
                    killAnim(tv_kill_count)
                } else if (flyType == FlyType.PLANE_BOSS) {
                    killBossCount++
                    tv_kill_boss_count.text = "击败 $killBossCount"
                    killAnim(tv_kill_boss_count)
                }
            }
        }
    }


    private fun killAnim(view: View) {
        if (view.id == lastAnimViewId) {
            killAnim2One?.cancel()
            killAnim2Zero?.cancel()
        } else {
            lastAnimViewId = view.id
        }
        killAnim2One = ValueAnimator.ofFloat(view.alpha, 1F).apply {
            addUpdateListener {
                view.alpha = it.animatedValue as Float
            }
            duration = 500
            doOnEnd {

                killAnim2Zero = ValueAnimator.ofFloat(view.alpha, 0F).apply {
                    addUpdateListener {
                        view.alpha = it.animatedValue as Float
                    }
                    duration = 1500
                    start()
                }
            }
            start()
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
