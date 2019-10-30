package com.biceps_studio.task_layout.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.biceps_studio.task_layout.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Glide.with(this).asGif().load("https://i.gifer.com/AGNB.gif").into(ivSplash)
    }

    override fun onResume() {
        super.onResume()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 5000)
    }
}
