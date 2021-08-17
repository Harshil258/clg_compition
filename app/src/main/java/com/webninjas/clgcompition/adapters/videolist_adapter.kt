package com.webninjas.clgcompition.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.webninjas.clgcompition.R
import com.webninjas.clgcompition.models.videolist_model
import com.webninjas.clgcompition.pref.MOBILE_NO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class videolist_adapter(var context: Context, var list: List<videolist_model>) :
    RecyclerView.Adapter<videolist_adapter.videolist_holder>() {

    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var isliked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): videolist_holder {
        var inflater: LayoutInflater? = LayoutInflater.from(parent.context)
        val view: View = inflater!!.inflate(R.layout.video_item, parent, false)
        return videolist_holder(view)
    }

    override fun onBindViewHolder(holder: videolist_holder, position: Int) {


        holder.like.setOnClickListener {
            managelike(list[position].compititionname, position, holder)
        }
        setlikes(list[position].compititionname, position, holder)


//        holder.videoview.setVideoURI(Uri.parse(list[position].videourl))
//        holder.videoview.resume()
    }


    override fun getItemCount(): Int {
        return list.size
    }

    private fun managelike(compititionname: String, position: Int, holder: videolist_holder) {
        Log.d("arhgerherth", holder.like.drawable.toString())
        Log.d("arhgerherth", context.getDrawable(R.drawable.ic_liked).toString())

        if (isliked) {
            Log.d("arhgerherth", "unliked")
            db.collection("competitions").document(compititionname).collection("Videos")
                .document(list[position].documentid)
                .collection("Likes").document(MOBILE_NO).delete()
            isliked = false
            holder.like.setImageDrawable(context.getDrawable(R.drawable.ic_notliked))
            Toast.makeText(context, "unliked", Toast.LENGTH_SHORT).show()

        } else {
//            val map: MutableMap<String, Any> = HashMap()
//            map["yourProperty"] = "yourValue"
            val current: String =
                SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            var map: MutableMap<String, String> = HashMap()
            map.put("mobile", MOBILE_NO)
            map.put("Time", current)
            db.collection("competitions").document(compititionname).collection("Videos")
                .document(list[position].documentid)
                .collection("Likes").document(MOBILE_NO).set(map)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        holder.like.setImageDrawable(context.getDrawable(R.drawable.ic_liked))
                        isliked = true

                        Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("segsgrrgrg", it.result.toString())
                }
        }
    }

    fun setlikes(compititionname: String, position: Int, holder: videolist_holder) {

        db.collection("competitions").document(compititionname).collection("Videos")
            .document(list[position].documentid)
            .collection("Likes").get().addOnCompleteListener {
                Log.d("sgfaerhaerh", "sucess")
                for (document in it.result?.documents!!) {
                    var number = document.id.toString()
                    Log.d("sgfaerhaerh", number)
                    Log.d("sgfaerhaerh", MOBILE_NO)
                    if (MOBILE_NO.toLong().equals(number.toLong())) {
                        holder.like.setImageDrawable(context.getDrawable(R.drawable.ic_liked))
                        isliked = true
                        Log.d("arhgertrherth", "liked  " + MOBILE_NO)
                    }
                }
            }
    }


    class videolist_holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoview: VideoView = itemView.findViewById(R.id.videoview)
        var like: ImageView = itemView.findViewById(R.id.like)


    }
}