package com.webninjas.clgcompition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.webninjas.clgcompition.pref.CLASSNAME
import com.webninjas.clgcompition.pref.ROLL_NO
import com.webninjas.clgcompition.pref.USERNAME
import com.webninjas.clgcompition.pref.getpref

class spalsh_activity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        Log.d("Arhrseerfh",getpref(this,"detailputed").toString())

        Handler().postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null) {
                startActivity(Intent(this@spalsh_activity, MainActivity::class.java))
                finish()
            } else {

                if (getpref(this,"detailputed") == "true"){
                    pref.MOBILE_NO = getpref(this, "MOBILE_NO")
                    USERNAME = getpref(this, "USERNAME")
                    ROLL_NO = getpref(this, "ROLL_NO")
                    CLASSNAME = getpref(this, "CLASSNAME")
                    startActivity(Intent(this@spalsh_activity, compition_list::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@spalsh_activity, get_userdetails::class.java))
                    finish()
                }
            }
        }, 3000)



    }
}