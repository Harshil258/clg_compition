package com.webninjas.clgcompition

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompition.adapters.videolist_adapter
import com.webninjas.clgcompition.models.videolist_model

class Main_video_list : AppCompatActivity() {

    lateinit var adapter: videolist_adapter
    lateinit var list: ArrayList<videolist_model>
    lateinit var recyclerview: RecyclerView
    lateinit var ic_option : ImageView
    lateinit var back : ImageView
    lateinit var db: FirebaseFirestore
    lateinit var MKLoader : MKLoader
    lateinit var novideos : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_video_list)

        list = ArrayList()
        MKLoader = findViewById(R.id.MKLoader)
        novideos = findViewById(R.id.novideos)
        MKLoader.visibility = View.VISIBLE

        ic_option = findViewById(R.id.ic_option)
        back = findViewById(R.id.back)

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = GridLayoutManager(this,2)
        recyclerview.layoutManager = layoutmanger
        adapter = videolist_adapter(this, list)
        recyclerview.adapter = adapter

        var competitionname = intent.getStringExtra("competitionname")

        back.setOnClickListener {
            finish()
        }

        ic_option.setOnClickListener {

            MKLoader.visibility = View.GONE

            var dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setContentView(R.layout.videolist_menu)

            var addvideos = dialog.findViewById<CardView>(R.id.addvideos)

            addvideos.setOnClickListener {
                Log.d("Agfasgeseg","clicked")
//                startActivity(Intent(this@Main_video_list,add_video_activity::class.java))
                var intent = Intent(this, get_permission::class.java)
                intent.putExtra("competitionname",competitionname)
                startActivity(intent)
            }

            dialog.show()

        }

        db = FirebaseFirestore.getInstance()
        db.collection("competitions").document(competitionname.toString()).collection("Videos")
            .get().addOnCompleteListener {
                MKLoader.visibility = View.GONE
                for (document in it.result!!.documents) {
                    Log.d("arsrgrsh", document.data.toString())
                    list.add(
                        videolist_model(
                            document.data?.get("url").toString(),
                            competitionname.toString(),
                            document.id.toString(),
                            false
                        )
                    )
                }
                if (list.size == 0){
                    novideos.visibility = View.VISIBLE
                }
                adapter.notifyDataSetChanged()
            }


    }
}