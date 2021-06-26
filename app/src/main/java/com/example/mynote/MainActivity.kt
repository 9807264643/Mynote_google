package com.example.mynote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mynote.DashboardActivity.Dashboard
import com.example.mynote.LoginActivity.Login
import com.example.mynote.LoginActivity.OtpProgressbar.CircleProgressBar
import com.example.mynote.Loginwithemail.EmailLogin
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cvp = findViewById<CircleProgressBar>(R.id.cpv)

        mAuth = FirebaseAuth.getInstance()


        /** if user is not authenticated, send him to loginAcctivity to authenticate first */



    }




}