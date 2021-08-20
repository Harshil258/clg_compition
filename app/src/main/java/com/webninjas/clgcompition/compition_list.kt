package com.webninjas.clgcompition

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompition.adapters.compition_list_adapter
import com.webninjas.clgcompition.models.competitions_model
import com.webninjas.clgcompition.pref.MOBILE_NO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class compition_list : AppCompatActivity() {

    lateinit var db: FirebaseFirestore

    lateinit var adapter: compition_list_adapter
    lateinit var list: ArrayList<competitions_model>
    lateinit var recyclerview: RecyclerView
    lateinit var MKLoader: MKLoader
    lateinit var ic_option: ImageView
    lateinit var nocompitition: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compition_list)
        list = ArrayList()

        db = FirebaseFirestore.getInstance()
        MKLoader = findViewById(R.id.MKLoader)
        nocompitition = findViewById(R.id.nocompitition)
        MKLoader.visibility = View.VISIBLE
        ic_option = findViewById(R.id.ic_option)

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutmanger
        adapter = compition_list_adapter(this, list)
        recyclerview.adapter = adapter


        ic_option.setOnClickListener {

            MKLoader.visibility = View.GONE

            var dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setContentView(R.layout.videolist_menu)

            var addvideos = dialog.findViewById<CardView>(R.id.addvideos)
            var profile = dialog.findViewById<CardView>(R.id.profile)
            var LOGOUT = dialog.findViewById<CardView>(R.id.LOGOUT)

            addvideos.visibility = View.GONE
            profile.visibility = View.VISIBLE
            LOGOUT.visibility = View.VISIBLE

            LOGOUT.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                pref.setpref(this, "USERNAME", "")
                pref.setpref(this, "ROLL_NO", "")
                pref.setpref(this, "CLASSNAME", "")
                pref.setpref(this, "CLG_NAME", "")

                pref.USERNAME = pref.getpref(this, "USERNAME").toString()
                pref.ROLL_NO = pref.getpref(this, "ROLL_NO").toString()
                pref.CLASSNAME = pref.getpref(this, "CLASSNAME").toString()
                pref.CLG_NAME = pref.getpref(this, "CLG_NAME").toString()

                Toast.makeText(this, "You are sign out", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            profile.setOnClickListener {
                val intent = Intent(this, Profile_activity::class.java)
                startActivity(intent)
            }




            dialog.show()

        }

        db.collection("competitions").document("drawing").collection("Videos")
            .whereEqualTo("number", MOBILE_NO).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("sghsrhgsrfhdsh", it.result?.documents.toString())
                } else {
                    Log.d("sghsrhgsrfhdsh", it.exception.toString())
                }
            }

        db.collection("competitions").get().addOnCompleteListener {
            if (it.isSuccessful) {
//                Log.d("sedgsggsrg", it.result!!.documents.toString())
                for (document in it.result!!) {

                    Log.d(
                        "sedgsgdxrhgsrg",
                        document.id + " => " + document.data.get("starttime").toString()
                    )


                    list.add(
                        competitions_model(
                            document.id,
                            document.data.get("logourl").toString(),
                            document.data.get("starttime").toString(),
                            document.data.get("endtime").toString(),
                            document.data.get("DATE").toString()
                        )
                    )

                    val currentTime: String =
                        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())


                }
                MKLoader.visibility = View.GONE

                if (list.size == 0) {
                    nocompitition.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }
        }

    }
}