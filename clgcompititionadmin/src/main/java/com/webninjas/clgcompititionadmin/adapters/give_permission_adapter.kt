package com.webninjas.clgcompititionadmin.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.webninjas.clgcompititionadmin.MySingleton
import com.webninjas.clgcompititionadmin.R
import com.webninjas.clgcompititionadmin.models.permission_model
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import javax.xml.transform.ErrorListener
import javax.xml.transform.TransformerException

class give_permission_adapter(var context: Context, var list: List<permission_model>) :
    RecyclerView.Adapter<give_permission_adapter.give_permission_holder>() {
    lateinit var database: DatabaseReference

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAzH5Qmss:APA91bEYcmYaL5qHxFh-qUM26eIP_U_8M2AMIqvnjj67xTBTsvjxskND-yQg5W_ZeCiECfSzyUDc_U01wifurDpEz0JygYBdaPAt5ZbXfvxT3M63zk9pOyUN32xpWE-AlYAWhA0QzTUq"
    private val contentType = "application/json"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): give_permission_holder {
        var inflater: LayoutInflater? = LayoutInflater.from(parent.context)
        val view: View = inflater!!.inflate(R.layout.permission_layout, parent, false)
        return give_permission_holder(view)
    }

    override fun onBindViewHolder(holder: give_permission_holder, position: Int) {
        holder.fullname.setText(list[position].USERNAME)
        holder.classname.setText(list[position].CLASSNAME)
        holder.clgname.setText(list[position].CLG_NAME)
        holder.rollno.setText(list[position].ROLL_NO)
        holder.mobilenumber.setText(list[position].mobilenumber)

        var trueoffalse = "false"
        trueoffalse = list[position].getpermiossion
        Log.d("ESGrsg", list[position].getpermiossion)

        if (list[position].getpermiossion.toString() == "true") {
            holder.YES.setBackgroundResource(R.drawable.button_back)
            holder.YES.setTextColor(Color.WHITE)
        } else {
            holder.NO.setBackgroundResource(R.drawable.button_back)
            holder.NO.setTextColor(Color.WHITE)
        }

        holder.YES.setOnClickListener {
            if (trueoffalse == "true") {
                Toast.makeText(context, "You have given permission already", Toast.LENGTH_SHORT)
                    .show()
            } else {
                database = FirebaseDatabase.getInstance().getReference("requests")
                database.child(list[position].mobilenumber).child("getpermiossion").setValue("true")
                    .addOnCompleteListener {
                        holder.YES.setBackgroundResource(R.drawable.button_back)
                        holder.YES.setTextColor(Color.WHITE)
                        holder.NO.setBackgroundResource(R.drawable.edittext_back)
                        holder.NO.setTextColor(Color.parseColor("#6443eb"))
                        trueoffalse = "true"

                        var TOPIC =
                            "/topics/${list[position].mobilenumber}" //topic has to match what the receiver subscribed to

                        val notification = JSONObject()
                        val notifcationBody = JSONObject()
                        try {
                            notifcationBody.put(
                                "title",
                                "Congrats"
                            )
                            notifcationBody.put(
                                "message",
                                "You granted to Upload Videos by Admin. Hope you win Competition"
                            )

                            notification.put("to", TOPIC)
                            notification.put("data", notifcationBody)
                        } catch (e: JSONException) {
                            Log.e("TAG", "onCreate: " + e.message)
                        }
                        sendNotification(notification)
                    }
            }
        }

        holder.NO.setOnClickListener {
            if (trueoffalse == "false") {
                Toast.makeText(
                    context,
                    "You have already denied for permission",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                database = FirebaseDatabase.getInstance().getReference("requests")
                database.child(list[position].mobilenumber).child("getpermiossion")
                    .setValue("false")
                    .addOnCompleteListener {
                        holder.YES.setBackgroundResource(R.drawable.edittext_back)
                        holder.YES.setTextColor(Color.parseColor("#6443eb"))
                        holder.NO.setBackgroundResource(R.drawable.button_back)
                        holder.NO.setTextColor(Color.WHITE)
                        trueoffalse = "false"
                    }
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun sendNotification(notification: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener<JSONObject?> { response ->
                Log.i("suces", "onResponse: $response")
                Toast.makeText(context, "Notification is on the way...", Toast.LENGTH_SHORT)
                    .show()
            },
            object : ErrorListener, Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show()
                    Log.i("TAG", "onErrorResponse: Didn't work")
                }

                override fun warning(p0: TransformerException?) {
                    Log.i("TAG", "onErrorResponse: Didn't work")
                }

                override fun error(p0: TransformerException?) {
                    Log.i("TAG", "onErrorResponse: Didn't work")
                }

                override fun fatalError(p0: TransformerException?) {
                    Log.i("TAG", "onErrorResponse: Didn't work")
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }


    class give_permission_holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullname: TextView = itemView.findViewById(R.id.fullname)
        var classname: TextView = itemView.findViewById(R.id.classname)
        var clgname: TextView = itemView.findViewById(R.id.clgname)
        var rollno: TextView = itemView.findViewById(R.id.rollno)
        var mobilenumber: TextView = itemView.findViewById(R.id.mobilenumber)
        var YES: TextView = itemView.findViewById(R.id.YES)
        var NO: TextView = itemView.findViewById(R.id.NO)
    }
}