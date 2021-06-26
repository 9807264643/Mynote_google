package com.example.mynote.Loginwithemail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mynote.R
import com.example.mynote.RegistrationActivity.Registration

class EmailLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)

       var txt_resgiternew = findViewById<TextView>(R.id.resgiternew)

        txt_resgiternew.setOnClickListener {
            var intent = Intent(this@EmailLogin, Registration::class.java)
            startActivity(intent)
        }
    }
}