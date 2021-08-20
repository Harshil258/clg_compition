package com.webninjas.clgcompition

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

object pref {

    var USERNAME : String = ""
    var ROLL_NO : String = ""
    var MOBILE_NO : String = ""
    var CLASSNAME : String = ""
    var CLG_NAME : String = ""
    var getpermiossion : String = ""



    fun setpref(context: Context, key: String, value: String) {
        val sharedPrefFile = "kotlinsharedpreference"
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key,value)
        editor.commit()
    }


    fun getpref(context: Context, key: String) : String{
        val sharedPrefFile = "kotlinsharedpreference"
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(sharedPrefFile, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString(key,"value")!!
    }


    var canlike = false
}