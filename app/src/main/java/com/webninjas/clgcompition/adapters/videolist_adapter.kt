package com.webninjas.clgcompition.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.webninjas.clgcompition.R
import com.webninjas.clgcompition.Video_open_Activity
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


//        holder.like.setOnClickListener {
//            managelike(list[position].compititionname, position, holder)
//        }
//        setlikes(list[position].compititionname, position, holder)

        Glide.with(context).load(list[position].videourl).into(holder.ImageView)
        holder.ImageView.setOnClickListener {
            var intent = Intent(context,Video_open_Activity::class.java)
            intent.putExtra("videourl",list[position].videourl)
            intent.putExtra("documentid",list[position].documentid)
            intent.putExtra("compititionname",list[position].compititionname)
            context.startActivity(intent)
        }


//        holder.videoview.setVideoURI(Uri.parse(list[position].videourl))
//        holder.videoview.resume()
    }


    override fun getItemCount(): Int {
        return list.size
    }

    class videolist_holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ImageView: ImageView = itemView.findViewById(R.id.ImageView)
    }
}