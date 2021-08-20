package com.webninjas.clgcompititionadmin

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.*
import java.util.concurrent.TimeUnit


class VerificationActivity : AppCompatActivity() {

    lateinit var verify: Button
    lateinit var enterotp: EditText
    lateinit var numbershow: TextView
    lateinit var resendotp: TextView
    private var mAuth: FirebaseAuth? = null
    private var id: kotlin.String? = null
    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        verify = findViewById(R.id.verify)
        enterotp = findViewById(R.id.enterotp)
        numbershow = findViewById(R.id.numbershow)
        resendotp = findViewById(R.id.resendotp)

        mAuth = FirebaseAuth.getInstance()
        numbershow.text = pref.MOBILE_NO
        sendVerificationCode()

        verify.setOnClickListener {
            if (TextUtils.isEmpty(enterotp.text.toString())) {
                Toast.makeText(this@VerificationActivity, "Enter Otp", Toast.LENGTH_SHORT)
                    .show()
            } else if (enterotp.text.toString().replace(" ", "").length !== 6) {
                Toast.makeText(this@VerificationActivity, "Enter right otp", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val credential = PhoneAuthProvider.getCredential(
                    id!!,
                    enterotp.text.toString().replace(" ", "")
                )
                signInWithPhoneAuthCredential(credential)
            }
        }


        resendotp.setOnClickListener(View.OnClickListener { sendVerificationCode() })

    }

    private fun sendVerificationCode() {

        object : CountDownTimer(60000, 1000) {
            override fun onTick(l: Long) {
                resendotp.text = (l / 1000).toString()
                resendotp.isEnabled = false
            }

            override fun onFinish() {
                resendotp.text = "Resend OTP"
                resendotp.isEnabled = true
            }
        }.start()

        Log.d("rhasrbhsewrh", pref.MOBILE_NO.toString() + "arsrgahwregwr")


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91${pref.MOBILE_NO}",  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            this,  // Activity (for callback binding)
            object : OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(id: String, forceResendingToken: ForceResendingToken) {
                    this@VerificationActivity.id = id
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@VerificationActivity, "Failed", Toast.LENGTH_SHORT).show()
                    Log.d("rhasrbhsewrh", e.toString())

                }
            })
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {

                    var docref: DocumentReference =
                        db.collection("users").document(pref.MOBILE_NO.toString())

                    docref.addSnapshotListener { value, error ->
                        Log.d("ersagwrgaerg", value.toString())
                        if (value?.exists() == true) {
                            startActivity(
                                Intent(
                                    this@VerificationActivity,
                                    get_userdetails::class.java
                                )
                            )
                            finish()
                        } else {

//                            MOBILE_NO = number.toString()
                            var intent = Intent(this, get_userdetails::class.java)
                            startActivity(intent)
//                            startActivity(
//                                Intent(
//                                    this@VerificationActivity,
//                                    get_userdetails::class.java
//                                )
//                            )
                            finish()
                        }
                        Log.d("aegaedgedg", error.toString())
                    }


                } else {
                    Toast.makeText(
                        this@VerificationActivity,
                        "Verification Filed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
    }
}