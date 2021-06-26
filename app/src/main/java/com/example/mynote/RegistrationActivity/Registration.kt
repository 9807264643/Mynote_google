package com.example.mynote.RegistrationActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mynote.Loginwithemail.EmailLogin
import com.example.mynote.R

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        var btn_regitration = findViewById<Button>(R.id.btn_regitration)

        btn_regitration.setOnClickListener {
            var intent = Intent(this@Registration, EmailLogin::class.java)
            startActivity(intent)
        }
    }
}


