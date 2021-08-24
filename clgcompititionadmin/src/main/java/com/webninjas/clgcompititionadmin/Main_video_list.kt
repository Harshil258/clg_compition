package com.webninjas.clgcompititionadmin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompititionadmin.adapters.videolist_adapter
import com.webninjas.clgcompititionadmin.models.videolist_model
import com.webninjas.clgcompititionadmin.pref.competitionname

class Main_video_list : AppCompatActivity() {

    lateinit var adapter: videolist_adapter
    var list: ArrayList<videolist_model> = ArrayList<videolist_model>()
    lateinit var recyclerview: RecyclerView
    lateinit var ic_option: ImageView
    lateinit var back: ImageView
    lateinit var db: FirebaseFirestore
    lateinit var MKLoader: MKLoader
    lateinit var novideos: TextView
    lateinit var header_main: TextView
    lateinit var swipetorefresh: SwipeRefreshLayout
    lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_video_list)

        MKLoader = findViewById(R.id.MKLoader)
        novideos = findViewById(R.id.novideos)
        header_main = findViewById(R.id.header_main)
        MKLoader.visibility = View.VISIBLE
        swipetorefresh = findViewById(R.id.swipetorefresh)
        dialog = Dialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog)
        db = FirebaseFirestore.getInstance()

        ic_option = findViewById(R.id.ic_option)
        back = findViewById(R.id.back)

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutmanger
        adapter = videolist_adapter(this, list)
        recyclerview.adapter = adapter

        header_main.setText(competitionname)
        back.setOnClickListener {
            finish()
        }
        getdata()


        var runnable = Runnable {


            getdata()
        }


        swipetorefresh.setOnRefreshListener {




            swipetorefresh.isRefreshing = false
        }



        ic_option.setOnClickListener {

            MKLoader.visibility = View.GONE

            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.videolist_menu, null, false)
            dialog.setTitle(null)
            dialog.setCancelable(true)
            dialog.setContentView(view)

            var addvideos = dialog.findViewById<CardView>(R.id.addvideos)
            var givepermission = dialog.findViewById<CardView>(R.id.givepermission)

            givepermission.visibility = View.VISIBLE

            addvideos.setOnClickListener {
                dialog.dismiss()
                Log.d("Agfasgeseg", "clicked")
//                startActivity(Intent(this@Main_video_list,add_video_activity::class.java))
                var intent = Intent(this, get_permission::class.java)
                startActivity(intent)
            }

            givepermission.setOnClickListener {
                dialog.dismiss()
                var intent = Intent(this, give_permission::class.java)
                startActivity(intent)
            }

            dialog.show()

        }

    }

    override fun onPause() {
        super.onPause()
        if (dialog != null && dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun getdata() {


        list.clear()

        db.collection("videos").whereEqualTo("compititionname", competitionname)
            .get()
            .addOnCompleteListener {
                MKLoader.visibility = View.GONE
                var item = 0
                var size = it.result?.documents?.size
                for (document in it.result!!.documents) {
                    item++
                    Log.d("Egfegsgrg", document.id)
                    var likes = ""
                    db.collection("videos")
                        .document(document.id)
                        .collection("Likes")
                        .get().addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d("arsrrhgrsh", it.result?.size().toString() + "e   gegeg")
                                likes = it.result?.size().toString() + " Likes"

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

                                            var model = videolist_model(
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

                                            list.add(model)
//                                adapter.notifyItemChanged(list.size)
                                            Log.d("Egfdgdgegsdgsdg", list.size.toString())
                                            Log.d("Egfdgdgegsdgsdg", item.toString())

                                            if (size == item){
                                                if (list.size == 0) {
                                                    novideos.visibility = View.VISIBLE
                                                } else {
                                                    novideos.visibility = View.GONE
                                                }
                                                Log.d("rshgerfhefrhfh", list.toString())
                                                var sortedList = list.sortedByDescending { it.likes }
                                                list.clear()
                                                list.addAll(sortedList)
                                                Log.d("rshgsfgerfhefrhfh", list.toString())

                                                adapter.notifyDataSetChanged()
                                            }
                                        }
                                        Log.d("Egfsdgsdg", list.size.toString())
                                    }

                            }
                        }

                    Log.d("Egfsdgsdg", list.size.toString() + "  werferf")


                    Log.d("arsrgrsh", document.data.toString())


                }


            }

            Log.d("Egfsdgsdg", list.size.toString() + "  wefhrferf")





//        Handler().postDelayed(Runnable() {
//            if (list.size == 0) {
//                novideos.visibility = View.VISIBLE
//            } else {
//                novideos.visibility = View.GONE
//            }
//            adapter.notifyDataSetChanged()
//        }, 1000)


        Log.d(
            "arsgregrhgrsh",
            list.size.toString() + "   liststize"
        )

    }
}