package com.webninjas.clgcompition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompition.adapters.videolist_adapter
import com.webninjas.clgcompition.models.videolist_model
import com.webninjas.clgcompition.pref.MOBILE_NO
import com.webninjas.clgcompition.pref.competitionname

class my_videos : AppCompatActivity() {

    lateinit var adapter: videolist_adapter
    lateinit var list: ArrayList<videolist_model>
    lateinit var recyclerview: RecyclerView
    lateinit var back : ImageView
    lateinit var db: FirebaseFirestore
    lateinit var MKLoader : MKLoader
    lateinit var novideos : TextView
    lateinit var header_main : TextView
    lateinit var swipetorefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_videos)

        list = ArrayList()
        MKLoader = findViewById(R.id.MKLoader)
        novideos = findViewById(R.id.novideos)
        MKLoader.visibility = View.VISIBLE
        header_main = findViewById(R.id.header_main)
        swipetorefresh = findViewById(R.id.swipetorefresh)
        db = FirebaseFirestore.getInstance()


        back = findViewById(R.id.back)

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = GridLayoutManager(this,2)
        recyclerview.layoutManager = layoutmanger
        adapter = videolist_adapter(this, list)
        recyclerview.adapter = adapter

        back.setOnClickListener {
            finish()
        }

        swipetorefresh.setOnRefreshListener {
            list.clear()
            getdata()
            swipetorefresh.isRefreshing = false
        }
        Log.d("egfwsegjhgrjgsgfwegr",list.size.toString())
        getdata()
        Log.d("egfwsegjhgrjgsgfwegr",list.size.toString())

    }

    private fun getdata() {
        list.clear()
        db.collection("videos").whereEqualTo("compititionname",pref.competitionname)
            .get()
            .addOnCompleteListener {
                MKLoader.visibility = View.GONE
                Log.d("egfwsegswegr",it.result.toString())
                Log.d("egfwsegswegr", it.result?.documents?.size.toString())

                var i = 0
                for (document in it.result!!.documents) {
                    i++
                    Log.d("arsssrgrsh", document.data.toString())
                    list.add(
                        videolist_model(
                            document.data?.get("url").toString(),
                            competitionname,
                            document.id,
                            false,
                            document.data?.get("number").toString(),
                            document.data?.get("videoname").toString()
                        )
                    )

                }

                if (list.size == 0){
                    novideos.visibility = View.VISIBLE
                }else{
                    novideos.visibility = View.GONE
                }
                adapter.notifyDataSetChanged()
            }

    }

}