package com.webninjas.clgcompition

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.webninjas.clgcompition.pref.getpref

class spalsh_activity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        Log.d("Arhrseerfh", getpref(this, "detailputed").toString())

        Handler().postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this@spalsh_activity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@spalsh_activity, get_userdetails::class.java))
                finish()
            }
        }, 3000)


    }
}