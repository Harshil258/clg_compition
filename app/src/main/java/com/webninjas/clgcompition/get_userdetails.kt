package com.webninjas.clgcompition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompition.pref.CLASSNAME
import com.webninjas.clgcompition.pref.CLG_NAME
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
    lateinit var clg_name: EditText
    lateinit var rollno: EditText

    lateinit var MKLoader : MKLoader
    lateinit var MKLoader1 : MKLoader

    var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_userdetails)

        submit = findViewById(R.id.submit)
        mobilenumber = findViewById(R.id.mobilenumber)
        fullname = findViewById(R.id.fullname)
        classname = findViewById(R.id.classname)
        clg_name = findViewById(R.id.CLG_NAME)
        rollno = findViewById(R.id.rollno)
        MKLoader = findViewById(R.id.MKLoader)
        MKLoader1 = findViewById(R.id.MKLoader1)
        MOBILE_NO = getpref(this, "MOBILE_NO")

//        setpref(this,"isfirsttime","true")

        Log.d("getSharedPreferenceswe", getpref(this, "Name").toString())
        Log.d("fdhsgzthd", MOBILE_NO + " rghserehth")

        db.collection("users").document(MOBILE_NO).get().addOnCompleteListener {

            if (it.isSuccessful) {

                Log.d("tdjsedtjstrjrt", it.result?.data?.contains("USERNAME").toString())
                Log.d("tdjsedtjstrjrt", it.result?.data?.values.toString())
                MKLoader.visibility = View.VISIBLE

                if (it.result?.data?.contains("USERNAME") == true){

                    setpref(this, "USERNAME", it.result?.data!!.get("USERNAME").toString())
                    setpref(this, "ROLL_NO", it.result?.data!!.get("ROLL_NO").toString())
                    setpref(this, "CLASSNAME", it.result?.data!!.get("CLASSNAME").toString())
                    setpref(this, "CLG_NAME", it.result?.data!!.get("CLG_NAME").toString())

                    USERNAME = getpref(this,"USERNAME").toString()
                    ROLL_NO = getpref(this,"ROLL_NO").toString()
                    CLASSNAME = getpref(this,"CLASSNAME").toString()
                    CLG_NAME = getpref(this,"CLG_NAME").toString()

                    fullname.setText(USERNAME)
                    rollno.setText(ROLL_NO)
                    classname.setText(CLASSNAME)
                    clg_name.setText(CLG_NAME)

                    Log.d("getSharedPrdvgenceswe", getpref(this, "CLG_NAME").toString())
                    MKLoader.visibility = View.INVISIBLE
                    MKLoader1.visibility = View.INVISIBLE
                    startActivity(Intent(this@get_userdetails, compition_list::class.java))
                    finish()

                }else{
                    Log.d("getShareftgj", getpref(this, "CLG_NAME").toString())
                    MKLoader1.visibility = View.GONE

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
                            CLG_NAME = clg_name.text.toString()

                            Log.d("getSharedPrefere", MOBILE_NO)

                            val data = hashMapOf(
                                "USERNAME" to USERNAME,
                                "ROLL_NO" to ROLL_NO,
                                "CLASSNAME" to CLASSNAME,
                                "CLG_NAME" to CLG_NAME
                            )
                            db.collection("users").document(MOBILE_NO).set(data)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        setpref(this, "USERNAME", fullname.text.toString())
                                        setpref(this, "ROLL_NO", rollno.text.toString())
                                        setpref(this, "MOBILE_NO", MOBILE_NO.toString())
                                        setpref(this, "CLASSNAME", classname.text.toString())
                                        setpref(this, "CLG_NAME", clg_name.text.toString())

                                        MKLoader.visibility = View.INVISIBLE
                                        startActivity(
                                            Intent(
                                                this@get_userdetails,
                                                compition_list::class.java
                                            )
                                        )
                                        finish()
                                        Toast.makeText(
                                            this@get_userdetails,
                                            "DATA is ADDED",
                                            Toast.LENGTH_SHORT
                                        )
                                    } else if (it.isCanceled) {
                                        MKLoader.visibility = View.INVISIBLE
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