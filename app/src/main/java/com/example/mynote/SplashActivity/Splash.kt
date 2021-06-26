package com.example.mynote.SplashActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mynote.LoginActivity.Login
import com.example.mynote.MainActivity
import com.example.mynote.R

class Splash : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            {
                val i = Intent(this@Splash, MainActivity::class.java)
                startActivity(i)
                finish()
            }, SPLASH_TIME_OUT)

    }
}