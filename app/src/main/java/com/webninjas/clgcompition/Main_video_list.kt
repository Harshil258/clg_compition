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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompition.adapters.videolist_adapter
import com.webninjas.clgcompition.models.videolist_model
import com.webninjas.clgcompition.pref.competitionname

class Main_video_list : AppCompatActivity() {

    lateinit var adapter: videolist_adapter
    lateinit var list: ArrayList<videolist_model>
    lateinit var recyclerview: RecyclerView
    lateinit var ic_option : ImageView
    lateinit var back : ImageView
    lateinit var db: FirebaseFirestore
    lateinit var MKLoader : MKLoader
    lateinit var novideos : TextView
    lateinit var header_main : TextView
    lateinit var swipetorefresh : SwipeRefreshLayout
    lateinit var dialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_video_list)

        list = ArrayList()
        MKLoader = findViewById(R.id.MKLoader)
        novideos = findViewById(R.id.novideos)
        MKLoader.visibility = View.VISIBLE
        header_main = findViewById(R.id.header_main)
        swipetorefresh = findViewById(R.id.swipetorefresh)
        dialog = Dialog(this,R.style.Theme_AppCompat_DayNight_Dialog)

        ic_option = findViewById(R.id.ic_option)
        back = findViewById(R.id.back)

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = GridLayoutManager(this,2)
        recyclerview.layoutManager = layoutmanger
        adapter = videolist_adapter(this, list)
        recyclerview.adapter = adapter
        header_main.setText(competitionname)

        var competitionname = intent.getStringExtra("competitionname")

        back.setOnClickListener {
            finish()
        }

        swipetorefresh.setOnRefreshListener {
            getdata()
            swipetorefresh.isRefreshing  = false
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

            addvideos.setOnClickListener {
                Log.d("Agfasgeseg","clicked")
//                startActivity(Intent(this@Main_video_list,add_video_activity::class.java))
                var intent = Intent(this, get_permission::class.java)
                intent.putExtra("competitionname", pref.competitionname)
                startActivity(intent)
            }

            dialog.show()

        }





    }

    private fun getdata() {
        list.clear()
        db = FirebaseFirestore.getInstance()
        db.collection("videos").whereEqualTo("compititionname",pref.competitionname)
            .get()
            .addOnCompleteListener {
                MKLoader.visibility = View.GONE
                for (document in it.result!!.documents) {
                    Log.d("arsrgrsh", document.data.toString())
                    list.add(
                        videolist_model(
                            document.data?.get("url").toString(),
                            competitionname.toString(),
                            document.id.toString(),
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

    override fun onPause() {
        super.onPause()
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }
}