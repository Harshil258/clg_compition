package com.webninjas.clgcompititionadmin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.webninjas.clgcompititionadmin.adapters.give_permission_adapter
import com.webninjas.clgcompititionadmin.models.permission_model

class give_permission : AppCompatActivity() {

    lateinit var adapter: give_permission_adapter
    lateinit var list: ArrayList<permission_model>
    lateinit var recyclerview: RecyclerView
    lateinit var database: DatabaseReference
    lateinit var norequests : TextView
    lateinit var swipetorefresh : SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_give_permission)

        list = ArrayList()
        norequests = findViewById(R.id.norequests)
        swipetorefresh = findViewById(R.id.swipetorefresh)

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = LinearLayoutManager(this,)
        recyclerview.layoutManager = layoutmanger
        adapter = give_permission_adapter(this, list)
        recyclerview.adapter = adapter


        database = FirebaseDatabase.getInstance().getReference("requests")

        swipetorefresh.setOnRefreshListener {
            getdetails()
            swipetorefresh.isRefreshing  = false
        }
        getdetails()


    }

    private fun getdetails() {
        list.clear()
        database.get()
            .addOnCompleteListener {
//                Log.d("ESGrsg", it.result!!.value.toString())

                it.result!!.children.forEach {
                    Log.d("ESdrfhtGrsg", it.value.toString())
                    Log.d("ESdrfhtGrsg", it.key.toString())
                    Log.d("ESdrfhtGrsg", it.child("ROLL_NO").value.toString())

                    list.add(
                        permission_model(
                            it.child("CLASSNAME").value.toString(),
                            it.child("CLG_NAME").value.toString(),
                            it.child("ROLL_NO").value.toString(),
                            it.child("USERNAME").value.toString(),
                            it.child("getpermiossion").value.toString(),
                            it.key.toString()
                        ))
                }
                if (list.size == 0){
                    norequests.visibility = View.VISIBLE
                }else{
                    norequests.visibility = View.GONE
                }
                adapter.notifyDataSetChanged()
            }
    }
}