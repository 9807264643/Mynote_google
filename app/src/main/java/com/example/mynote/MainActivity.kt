package com.example.mynote

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mynote.DashboardActivity.Dashboard
import com.example.mynote.LoginActivity.Login
import com.example.mynote.LoginActivity.OtpProgressbar.CircleProgressBar
import com.example.mynote.Loginwithemail.EmailLogin
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var frag: Fragment
    lateinit var fragments: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer)
        setSupportActionBar(toolbar)
        val toogle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close )
       drawerLayout.addDrawerListener(toogle)
        toogle.syncState()


    }


}