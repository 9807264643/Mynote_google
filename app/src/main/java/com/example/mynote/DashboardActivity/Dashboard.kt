package com.example.mynote.DashboardActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mynote.LoginActivity.Login
import com.example.mynote.R
import com.example.mynote.DashboardActivity.ViewModel.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth


class Dashboard : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    lateinit var txt_name: TextView
    lateinit var txt_email: TextView
    lateinit var img_google: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        txt_name = findViewById<TextView>(R.id.txt_names)
        txt_email = findViewById<TextView>(R.id.txt_emails)
        val txt_id = findViewById<TextView>(R.id.txt_ids)
        img_google = findViewById<ImageView>(R.id.img_google)

        val btn_sign_out = findViewById<Button>(R.id.btn_sign_out)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        txt_name.text = currentUser!!.displayName
        txt_email.text = currentUser.email
        txt_id.text = currentUser.uid

        Glide.with(this).load(currentUser.photoUrl).into(img_google)

        btn_sign_out.setOnClickListener {
            mAuth.signOut()
            val signoutIntent = Intent(this, Login::class.java)
            startActivity(signoutIntent)
            finish()
        }
//        getValueFromDatabase()

        // otp login output detail
        val viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        viewModel.getDetails(this)

        viewModel.getLiveData().observe(this, Observer {
            txt_name.text = it!!.name
            txt_email.text = it.email
//            Glide.with(this).load(it.email).into(img_google)
        })

    }

    private fun getValueFromDatabase() {


//        db.collection("Users")
//           .get()
//            .addOnSuccessListener {
//                if (!it.isEmpty) {
//                    val i = it.toObjects(UserIformation::class.java)
//
//                    for (user in i) {
//                        Log.d("email",user.email)
//                    }
//
//                }
//            }


    }
}