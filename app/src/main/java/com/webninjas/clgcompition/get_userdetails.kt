package com.webninjas.clgcompition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.webninjas.clgcompition.pref.CLASSNAME
import com.webninjas.clgcompition.pref.MOBILE_NO
import com.webninjas.clgcompition.pref.ROLL_NO
import com.webninjas.clgcompition.pref.USERNAME
import com.webninjas.clgcompition.pref.getpref
import com.webninjas.clgcompition.pref.setpref


class get_userdetails : AppCompatActivity() {


    lateinit var submit: Button
    lateinit var mobilenumber: TextView
    lateinit var fullname: EditText
    lateinit var classname: EditText
    lateinit var rollno: EditText

    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_userdetails)

        submit = findViewById(R.id.submit)
        mobilenumber = findViewById(R.id.mobilenumber)
        fullname = findViewById(R.id.fullname)
        classname = findViewById(R.id.classname)
        rollno = findViewById(R.id.rollno)
        MOBILE_NO = getpref(this, "MOBILE_NO")

//        setpref(this,"isfirsttime","true")

        Log.d("getSharedPreferenceswe", getpref(this, "Name").toString())
        Log.d("fdhsgzthd", MOBILE_NO + " rghserehth")

        db.collection("users").document(MOBILE_NO).get().addOnCompleteListener {

            if (it.isSuccessful) {

                Log.d("tdjsedtjstrjrt", it.result?.data?.contains("USERNAME").toString())

                if (it.result?.data?.contains("USERNAME") == true){

                    setpref(this, "USERNAME", it.result?.data!!.get("USERNAME").toString())
                    setpref(this, "ROLL_NO", it.result?.data!!.get("ROLL_NO").toString())
                    setpref(this, "CLASSNAME", it.result?.data!!.get("CLASSNAME").toString())
                    setpref(this, "detailputed", "true")
                    startActivity(Intent(this@get_userdetails, compition_list::class.java))

                }else{

                    mobilenumber.text = MOBILE_NO
                    submit.setOnClickListener {

                        if (fullname.text.isEmpty()) {
                            Toast.makeText(
                                this@get_userdetails,
                                "Enter Full name in Field",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else if (rollno.text.isEmpty()) {
                            Toast.makeText(
                                this@get_userdetails,
                                "Enter Roll No... in Field",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else if (classname.text.isEmpty()) {
                            Toast.makeText(
                                this@get_userdetails,
                                "Enter class name ... in Field",
                                Toast.LENGTH_SHORT
                            )
                        } else {

                            USERNAME = fullname.text.toString()
                            ROLL_NO = rollno.text.toString()
                            CLASSNAME = classname.text.toString()

                            Log.d("getSharedPrefere", MOBILE_NO)

                            val data = hashMapOf(
                                "USERNAME" to USERNAME,
                                "ROLL_NO" to ROLL_NO,
                                "CLASSNAME" to CLASSNAME
                            )
                            db.collection("users").document(MOBILE_NO).set(data)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        setpref(this, "USERNAME", fullname.text.toString())
                                        setpref(this, "ROLL_NO", rollno.text.toString())
                                        setpref(this, "MOBILE_NO", MOBILE_NO.toString())
                                        setpref(this, "CLASSNAME", classname.text.toString())
                                        setpref(this, "detailputed", "true")

                                        startActivity(
                                            Intent(
                                                this@get_userdetails,
                                                compition_list::class.java
                                            )
                                        )
                                        Toast.makeText(
                                            this@get_userdetails,
                                            "DATA is ADDED",
                                            Toast.LENGTH_SHORT
                                        )
                                    } else if (it.isCanceled) {
                                        Toast.makeText(
                                            this@get_userdetails,
                                            "Something went worng!!",
                                            Toast.LENGTH_SHORT
                                        )
                                    }
                                }
                        }
                    }
                }


            } else {


            }


        }


    }
}