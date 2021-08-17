package com.webninjas.clgcompition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.webninjas.clgcompition.adapters.compition_list_adapter
import com.webninjas.clgcompition.models.competitions_model
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class compition_list : AppCompatActivity() {

    lateinit var db : FirebaseFirestore

    lateinit var adapter: compition_list_adapter
    lateinit var list: ArrayList<competitions_model>
    lateinit var recyclerview: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compition_list)
        list = ArrayList()

        db = FirebaseFirestore.getInstance()

        recyclerview = findViewById(R.id.recyclerview)
        var layoutmanger = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutmanger
        adapter = compition_list_adapter(this, list)
        recyclerview.adapter = adapter


        db.collection("competitions").get().addOnCompleteListener {
            if (it.isSuccessful){
//                Log.d("sedgsggsrg", it.result!!.documents.toString())
                for (document in it.result!!) {

                    Log.d("sedgsgdxrhgsrg", document.id + " => " + document.data.get("starttime").toString())

                    list.add(competitions_model(document.id,
                        document.data.get("PHOTO_URL").toString(),
                        document.data.get("starttime").toString(),
                        document.data.get("endtime").toString(),
                        document.data.get("DATE").toString()
                    ))

                    val currentTime: String =
                        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())


                }

                adapter.notifyDataSetChanged()
            }
        }

    }
}