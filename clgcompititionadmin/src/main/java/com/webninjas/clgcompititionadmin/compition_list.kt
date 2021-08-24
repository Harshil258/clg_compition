package com.webninjas.clgcompititionadmin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompititionadmin.adapters.compition_list_adapter
import com.webninjas.clgcompititionadmin.models.competitions_model
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
    lateinit var swipetorefresh: SwipeRefreshLayout
    lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compition_list)
        list = ArrayList()

        dialog = Dialog(this,R.style.ThemeOverlay_MaterialComponents_Dialog)

        db = FirebaseFirestore.getInstance()
        MKLoader = findViewById(R.id.MKLoader)
        nocompitition = findViewById(R.id.nocompitition)
        MKLoader.visibility = View.VISIBLE
        ic_option = findViewById(R.id.ic_option)
        swipetorefresh = findViewById(R.id.swipetorefresh)

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutmanger
        adapter = compition_list_adapter(this, list)
        recyclerview.adapter = adapter

        swipetorefresh.setOnRefreshListener {
            getdata()
            swipetorefresh.isRefreshing = false
        }
        getdata()
        ic_option.setOnClickListener {

            MKLoader.visibility = View.GONE

            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.videolist_menu, null, false)
            dialog.setTitle(null)
            dialog.setCancelable(true)
            dialog.setContentView(view)

            var addvideos = dialog.findViewById<CardView>(R.id.addvideos)
            var profile = dialog.findViewById<CardView>(R.id.profile)
            var LOGOUT = dialog.findViewById<CardView>(R.id.LOGOUT)
            var addcompitition = dialog.findViewById<CardView>(R.id.addcompitition)

            addvideos.visibility = View.GONE
            profile.visibility = View.VISIBLE
            LOGOUT.visibility = View.VISIBLE
            addcompitition.visibility = View.VISIBLE


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

                dialog.dismiss()
                Toast.makeText(this, "You are sign out", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            profile.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, Profile_activity::class.java)
                startActivity(intent)
            }

            addcompitition.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(this, addcompitition_activity::class.java)
                startActivity(intent)
            }


            dialog.show()

        }


    }

    private fun getdata() {
        list.clear()
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
                } else {
                    nocompitition.visibility = View.GONE
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }
}