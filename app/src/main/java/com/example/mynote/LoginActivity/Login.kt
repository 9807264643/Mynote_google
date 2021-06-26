package com.example.mynote.LoginActivity

import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.chaos.view.PinView
import com.example.mynote.DashboardActivity.Dashboard
import com.example.mynote.LoginActivity.OtpProgressbar.CircleProgressBar
import com.example.mynote.Loginwithemail.EmailLogin
import com.example.mynote.MainActivity
import com.example.mynote.R
import com.example.mynote.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class Login : AppCompatActivity() {
    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

// otp
    lateinit var dialog_verifying: AlertDialog
    lateinit var dialog_user_detail: AlertDialog

    lateinit var tvLogin: TextView

    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null

    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var dialog_otp: AlertDialog
    private var mVerificationId: String? = null

    //Authentication Variable
    lateinit var otpAuth: FirebaseAuth
    lateinit var edit_phone: EditText

    val users = mAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this@Login)

        val rel_google = findViewById<RelativeLayout>(R.id.rel_google)
         edit_phone = findViewById<EditText>(R.id.edit_phone)
        val btn_sendOTP = findViewById<Button>(R.id.btn_sendOTP)

        getSession()


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // firebase Auth instance
        mAuth = FirebaseAuth.getInstance()

        rel_google!!.setOnClickListener {
            signIn()
        }

        var btn_email = findViewById<Button>(R.id.btn_email)
        btn_email.setOnClickListener {
            val intent = Intent(this@Login, EmailLogin::class.java)
            startActivity(intent)
        }


        // otp

        otpAuth = FirebaseAuth.getInstance()
        val user = otpAuth.currentUser

        btn_sendOTP.setOnClickListener {
            if (edit_phone.text.toString().isEmpty()){
                edit_phone.error = "please enter valid mobile number"
            } else if (edit_phone.text.toString().length != 10) {
                edit_phone.error = "Please enter valid Mobile Number"
            } else {

                val inflater = getLayoutInflater()
                val alertLayout = inflater.inflate(R.layout.otp_progressbar_dailog, null)
                val alert = AlertDialog.Builder(this@Login)
                alert.setView(alertLayout)
                alert.setCancelable(false)
                dialog_verifying = alert.create()
//                alert.show()

                sendCode()
            }

        }


        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(Credential: PhoneAuthCredential) {
                //        Toast.makeText(applicationContext,"Verfication Process",Toast.LENGTH_SHORT).show()

                signInWithPhoneAuthCredential(Credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(
                    applicationContext,
                    "Something Went Wrong, Please Try After Some Time",
                    Toast.LENGTH_SHORT
                ).show()


                Log.d("Failure", e.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token

            }
        }

    }

// google
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            val exception = task.exception

            if (task.isSuccessful){

                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("Login", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("Login", "Google sign in failed", e)
                }

            }else{
                Log.w("Login",exception.toString())
            }

        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithCredential:success")
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                }
            }
    }


    // otp login start

    private fun sendCode() {
        val phoneNumber = "+1" + edit_phone!!.text.toString()

        createOTPEnterDialog(phoneNumber)

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            mCallbacks
        )        // OnVerificationStateChangedCallbacks

    }

    private fun createOTPEnterDialog(phoneNumber: String) {

        val inflater = getLayoutInflater()
        val alertLayout = inflater.inflate(R.layout.activity_otp_authentication, null)
        alertLayout.findViewById<TextView>(R.id.phonenumberText).text = phoneNumber


        alertLayout.findViewById<CircleProgressBar>(R.id.cpv)
            .setProgressFormatter { progress, max -> progress.toString() }

        alertLayout.findViewById<TextView>(R.id.tvDidntGotCode).setOnClickListener {
            dialog_otp.dismiss()
            sendCode()
        }

        setProgress(alertLayout.findViewById<CircleProgressBar>(R.id.cpv), alertLayout.findViewById<TextView>(R.id.tvDidntGotCode))

        alertLayout.findViewById<Button>(R.id.verifyCodeButton)!!.setOnClickListener {

            val verificationCode = alertLayout.findViewById<PinView>(R.id.pinView)!!.text!!.toString()

            if (verificationCode.isEmpty()) {
                Toast.makeText(this@Login, "Enter verification code", Toast.LENGTH_SHORT)
                    .show()
//                Utils.showSnackMSG(alertLayout.parentAuth,"Please Enter Verification Code")

            } else {

                dialog_verifying.show()

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    val credential = PhoneAuthProvider.getCredential(
                        mVerificationId.toString(),
                        verificationCode
                    )
                    signInWithPhoneAuthCredential(credential)
                }, 3000)

            }
        }

        val showOTP = AlertDialog.Builder(this!!)
        showOTP.setView(alertLayout)
        showOTP.setCancelable(false)
        dialog_otp = showOTP.create()
        dialog_otp.show()

        alertLayout.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
            dialog_otp.dismiss()
        }

    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(Authentication.TAG, "signInWithCredential:success")

                    Toast.makeText(this, "Your Mobile Number has been verified. Please enter more details and create your Account.",
                        Toast.LENGTH_LONG).show()

//                    llDetails.visibility = View.VISIBLE
//                    llOTP.visibility = View.GONE

                    dialog_otp.dismiss()
                    dialog_verifying.dismiss()

                    val inflater = getLayoutInflater()
                    val alertLayout = inflater.inflate(R.layout.user_details, null)
                    val alert = AlertDialog.Builder(this@Login)
                    alert.setView(alertLayout)
                    alert.setCancelable(false)
                    dialog_user_detail = alert.create()
                     alert.show()
                    val user_name = alertLayout.findViewById<EditText>(R.id.edit_name)
                    val user_email = alertLayout.findViewById<EditText>(R.id.edit_email)
                    val user_phone = alertLayout.findViewById<EditText>(R.id.edit_phones)
                    val btn_submit = alertLayout.findViewById<Button>(R.id.btn_submit)

                    btn_submit.setOnClickListener {
                        uploadDataToFirebase(user_name,user_email,user_phone)
                    }




                } else {

                    dialog_verifying.dismiss()
                    dialog_otp.dismiss()
                    Toast.makeText(this@Login, "Incorrect OTP", Toast.LENGTH_SHORT).show()
//                    Utils.showSnackMSG(lottieSelectImage,"Incorrect OTP")

//                    Log.w(Authentication.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                    }
                }
            }
    }

    private fun uploadDataToFirebase(user_name: EditText, user_email: EditText, user_phone: EditText) {
        val name = user_name.text.toString()
        val email = user_email.text.toString()
        val phone = user_phone.text.toString()

        val items = HashMap<String, Any>()
        items.put("name", name)
        items.put("email",email)
        items.put("phoneno", phone)

        val db = FirebaseFirestore.getInstance()


        db.collection("Users").document(phone)
            .set(items).addOnSuccessListener {
                Utils.writeStringToPreferences("name", name, this)
                Utils.writeStringToPreferences("email", email, this)
                Utils.writeStringToPreferences("phoneno", phone, this)
                dialog_user_detail.dismiss()
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()

            }.addOnFailureListener {
                Toast.makeText(this, "Something Went Wrong!!", Toast.LENGTH_LONG)
                    .show()

            }
    }



    private fun setProgress(cpv: CircleProgressBar, tvDidntGotCode: TextView) {
        val animator = ValueAnimator.ofInt(100, 0)
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            cpv.setProgress(progress)

            if (cpv.progress == 0) {
                tvDidntGotCode.visibility = View.VISIBLE
                cpv.visibility = View.GONE
            }

        }
        //        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.duration = 30000
        animator.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAffinity(this)
    }

    private fun getSession(){
        Handler().postDelayed({
//            setProgressbar(cvp)
            if(users != null){
                val mainActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(mainActivityIntent)
                finish()
            }else{
                val loginActivityIntent = Intent(this, Login::class.java)
                startActivity(loginActivityIntent)
                finish()
            }

        }, 2000)
    }

    private fun setProgressbar(cpv: CircleProgressBar){
        cpv.setProgress(100)

    }

}