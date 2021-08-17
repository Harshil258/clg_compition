package com.webninjas.clgcompition

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.webninjas.clgcompition.adapters.videolist_adapter
import com.webninjas.clgcompition.models.videolist_model

class Main_video_list : AppCompatActivity() {

    lateinit var adapter: videolist_adapter
    lateinit var list: ArrayList<videolist_model>
    lateinit var recyclerview: RecyclerView

    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_video_list)

        list = ArrayList()

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutmanger
        adapter = videolist_adapter(this, list)
        recyclerview.adapter = adapter

        var competitionname = intent.getStringExtra("competitionname")

        db = FirebaseFirestore.getInstance()
        db.collection("competitions").document(competitionname.toString()).collection("Videos")
            .get().addOnCompleteListener {

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
                adapter.notifyDataSetChanged()
            }


    }
}