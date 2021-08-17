package com.webninjas.clgcompition

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import android.content.Intent

import android.widget.Toast

import android.text.TextUtils
import com.webninjas.clgcompition.pref.MOBILE_NO

class MainActivity : AppCompatActivity() {

    lateinit var sendotp: Button
    lateinit var phonenumber: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendotp = findViewById(R.id.sendotp)
        phonenumber = findViewById(R.id.phonenumber)

        sendotp.setOnClickListener {

            if (TextUtils.isEmpty(phonenumber.text.toString())) {
                Toast.makeText(this, "Enter No ....", Toast.LENGTH_SHORT).show()
            } else if (phonenumber.getText().toString().replace(" ", "").length !== 10) {
                Toast.makeText(this, "Enter Correct No ...", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, VerificationActivity::class.java)
                pref.setpref(this, "MOBILE_NO", phonenumber.text.toString())
                MOBILE_NO = phonenumber.text.toString()
                intent.putExtra("number", phonenumber.text.toString())
                startActivity(intent)
                finish()
            }

        }


    }
}