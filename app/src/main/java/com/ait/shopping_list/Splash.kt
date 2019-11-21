package com.ait.shopping_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.splash_screen.*
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.view.animation.Animation
import android.widget.Toast


class Splash : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH : Long = 5000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)


        var fadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_demo)
        var fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        splashscreen.startAnimation(fadeOut)

        fadeOut.setAnimationListener(
            object: Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    logo.visibility = View.VISIBLE
                    logo.startAnimation(fadeIn)
                }

                override fun onAnimationStart(p0: Animation?) {

                }
            }
        )
        fadeIn.setAnimationListener(
            object: Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    val mainIntent = Intent(this@Splash, ScrollingActivity::class.java)

                    this@Splash.startActivity(mainIntent)
                    this@Splash.finish()
                }

                override fun onAnimationStart(p0: Animation?) {

                }
            }
        )

    }
}
