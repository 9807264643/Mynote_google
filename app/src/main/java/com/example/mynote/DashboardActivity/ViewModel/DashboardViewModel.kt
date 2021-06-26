package com.example.mynote.DashboardActivity.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynote.DashboardActivity.Dashboard
import com.example.mynote.UserIformation
import com.example.mynote.Utils
import com.google.firebase.firestore.FirebaseFirestore

class DashboardViewModel : ViewModel() {

    private var userDetails = MutableLiveData<UserIformation>()

    fun getLiveData () : LiveData<UserIformation> {
        return userDetails
    }

    fun getDetails(context: Dashboard) {
        val db = FirebaseFirestore.getInstance()
        val mobileno = Utils.getStringFromPreferences("phoneno", "", context)

        db.collection("Users")
            .document(mobileno!!).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val i = it.toObject(UserIformation::class.java)

                    userDetails.value = i


                }
            }
    }

}