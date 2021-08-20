package com.webninjas.clgcompititionadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.webninjas.clgcompititionadmin.pref.MOBILE_NO

class Profile_activity : AppCompatActivity() {

    lateinit var mobilenumber: TextView
    lateinit var fullname: TextView
    lateinit var classname: TextView
    lateinit var clg_name: TextView
    lateinit var rollno: TextView
    lateinit var back : ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mobilenumber = findViewById(R.id.mobilenumber)
        fullname = findViewById(R.id.fullname)
        classname = findViewById(R.id.classname)
        clg_name = findViewById(R.id.CLG_NAME)
        rollno = findViewById(R.id.rollno)
        back = findViewById(R.id.back)

        fullname.setText(pref.USERNAME)
        rollno.setText(pref.ROLL_NO)
        classname.setText(pref.CLASSNAME)
        clg_name.setText(pref.CLG_NAME)
        mobilenumber.setText(MOBILE_NO)


        fullname.isClickable = false
        rollno.isClickable = false
        classname.isClickable = false
        clg_name.isClickable = false
        mobilenumber.isClickable = false

        back.setOnClickListener {
            finish()
        }

    }
}