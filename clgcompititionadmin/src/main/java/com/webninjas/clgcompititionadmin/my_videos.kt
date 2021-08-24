package com.webninjas.clgcompititionadmin

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompititionadmin.adapters.videolist_adapter
import com.webninjas.clgcompititionadmin.models.videolist_model
import com.webninjas.clgcompititionadmin.pref.CLG_NAME
import com.webninjas.clgcompititionadmin.pref.MOBILE_NO
import com.webninjas.clgcompititionadmin.pref.ROLL_NO
import com.webninjas.clgcompititionadmin.pref.USERNAME
import com.webninjas.clgcompititionadmin.pref.competitionname

class my_videos : AppCompatActivity() {

    lateinit var adapter: videolist_adapter
    var list: ArrayList<videolist_model> = ArrayList()
    lateinit var recyclerview: RecyclerView
    lateinit var back: ImageView
    lateinit var db: FirebaseFirestore
    lateinit var MKLoader: MKLoader
    lateinit var novideos: TextView
    lateinit var header_main: TextView
    lateinit var swipetorefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_videos)
        db = FirebaseFirestore.getInstance()

        MKLoader = findViewById(R.id.MKLoader)
        novideos = findViewById(R.id.novideos)
        MKLoader.visibility = View.VISIBLE
        header_main = findViewById(R.id.header_main)
        swipetorefresh = findViewById(R.id.swipetorefresh)
        swipetorefresh.setOnRefreshListener {
            getdata()
            swipetorefresh.isRefreshing = false
        }
        back = findViewById(R.id.back)
        getdata()

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutmanger
        adapter = videolist_adapter(this, list)
        recyclerview.adapter = adapter
        header_main.setText(pref.competitionname)

        back.setOnClickListener {
            finish()
        }


    }

    private fun getdata() {

        list.clear()
        db.collection("videos").whereEqualTo("number", MOBILE_NO)
            .get()
            .addOnCompleteListener {
                MKLoader.visibility = View.GONE
                var item = 0
                var size = it.result?.documents?.size
                for (document in it.result!!.documents) {
                    item++
                    db.collection("videos")
                        .document(document.id)
                        .collection("Likes")
                        .get().addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d("arsrrhgrsh", it.result?.size().toString() + "e   gegeg")
                                var likes = it.result?.size().toString() + "Likes"
//
                                db.collection("users")
                                    .document(document.data?.get("number").toString()).get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Log.d(
                                                "adgdgrsrrhgrsh",
                                                it.result?.data.toString()
                                            )

                                            var name = it.result?.get("CLASSNAME").toString()
                                            var rollno = it.result?.get("ROLL_NO").toString()
                                            var clgname = it.result?.get("CLG_NAME").toString()

                                            Log.d(
                                                "arsrrhgrsh",
                                                it.result?.get("CLG_NAME").toString() + "clgname"
                                            )

                                            list.add(
                                                videolist_model(
                                                    document.data?.get("url").toString(),
                                                    competitionname.toString(),
                                                    document.id.toString(),
                                                    false,
                                                    document.data?.get("number").toString(),
                                                    document.data?.get("videoname").toString(),
                                                    likes,
                                                    name,
                                                    rollno,
                                                    clgname
                                                )
                                            )

                                        }
                                        Log.d(
                                            "arstjregfeegrhgrsh",
                                            list.size.toString() + "   liststize"
                                        )
                                        if (size == item){
                                            if (list.size == 0) {
                                                novideos.visibility = View.VISIBLE
                                            } else {
                                                novideos.visibility = View.GONE
                                            }
                                            adapter.notifyDataSetChanged()
                                        }
                                    }
                            }
                        }



                    Log.d("arsrgrsh", document.data.toString())
                }


            }

        Log.d(
            "arsgregrhgrsh",
            list.size.toString() + "   liststize"
        )



    }

}