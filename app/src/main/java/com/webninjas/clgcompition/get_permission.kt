package com.webninjas.clgcompition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompition.pref.MOBILE_NO
import com.webninjas.clgcompition.pref.USERNAME
import com.webninjas.clgcompition.pref.getpref
import com.webninjas.clgcompition.pref.setpref

class get_permission : AppCompatActivity() {

    lateinit var yes: TextView
    lateinit var no: TextView
    lateinit var warning: TextView
    lateinit var database: DatabaseReference
    lateinit var MKLoader : MKLoader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_permission)

        database = FirebaseDatabase.getInstance().getReference("requests")

        yes = findViewById(R.id.yes)
        no = findViewById(R.id.no)
        warning = findViewById(R.id.warning)
        MKLoader = findViewById(R.id.MKLoader)


        database.child(MOBILE_NO).get()
            .addOnCompleteListener() { value ->
                if (value.isSuccessful) {
                    Log.d("ARherheh", value.result?.child("getpermiossion")?.value.toString())
                    if (value.result?.child("getpermiossion")?.value.toString() == "true") {
                        MKLoader.visibility = View.INVISIBLE
                        var intent = Intent(this, choose_activity::class.java)
                        startActivity(intent)
                        finish()

                    } else if (value.result?.child("getpermiossion")?.value.toString() == "false") {
                        MKLoader.visibility = View.INVISIBLE
                        yes.alpha = 0.6f
                        yes.isEnabled = false
                        warning.visibility = View.VISIBLE

                        no.setOnClickListener {
                            finish()
                        }

                    } else {
                        MKLoader.visibility = View.INVISIBLE
                        no.setOnClickListener {
                            finish()
                        }
                        Log.d("ARherheregrheh", getpref(this, "permission_msg").toString())

                        yes.setOnClickListener {
                            Log.d("ARherherrheh", value.result.toString())
                            Log.d("ARherhersdrgrheh", USERNAME + " AEgf")

                            val data = hashMapOf(
                                "USERNAME" to pref.USERNAME,
                                "ROLL_NO" to pref.ROLL_NO,
                                "CLASSNAME" to pref.CLASSNAME,
                                "CLG_NAME" to pref.CLG_NAME,
                                "getpermiossion" to "false"
                            )

                            database.child(MOBILE_NO).setValue(data)
                                .addOnCompleteListener { value ->
                                    if (value.isSuccessful) {
                                        Log.d("ARherheh", value.result.toString())
                                        yes.alpha = 0.6f
                                        yes.isEnabled = false
                                        warning.visibility = View.VISIBLE
                                    } else {

                                    }
                                }
                        }


                    }
                } else {

                    Log.d("ARherheh", value.exception.toString())
                }
            }


    }
}