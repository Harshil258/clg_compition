package com.webninjas.clgcompititionadmin.adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.skydoves.progressview.ProgressView
import com.tuyenmonkey.mkloader.MKLoader
import com.webninjas.clgcompititionadmin.Main_video_list
import com.webninjas.clgcompititionadmin.R
import com.webninjas.clgcompititionadmin.models.competitions_model
import com.webninjas.clgcompititionadmin.pref.competitionname

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


public class compition_list_adapter(var context: Context, list: ArrayList<competitions_model>) :
    RecyclerView.Adapter<compition_list_adapter.compitionholder>() {

    var datalist = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): compitionholder {
        var inflater: LayoutInflater? = LayoutInflater.from(parent.context)
        val view: View = inflater!!.inflate(R.layout.categories_item, parent, false)
        return compitionholder(view)
    }

    override fun onBindViewHolder(holder: compitionholder, position: Int) {

        holder.categori_name.text = datalist[position].competition_name
        Glide.with(context).load(datalist[position].logourl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.MKLoader.visibility = View.GONE
                    Toast.makeText(context,"Something went Wrong...", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.MKLoader.visibility = View.GONE
                    return false
                }

            }).into(holder.logo)


        val simpleDateFormat = SimpleDateFormat("hh:mm a")
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        Log.d("Efgasrdfhegsg",currentDate.toString())
        Log.d("Efgasrdfhegsg",datalist[position].DATE.toString())

        if (datalist[position].DATE == currentDate){

            val current: String =
                SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            var starttime = TimeUnit.MILLISECONDS.toMinutes(simpleDateFormat.parse(datalist[position].starttime).time)
            var endtime = TimeUnit.MILLISECONDS.toMinutes(simpleDateFormat.parse(datalist[position].endtime).time)
            var currtime = TimeUnit.MILLISECONDS.toMinutes(simpleDateFormat.parse(current).time)
            var progressbarlength = endtime - starttime

            Log.d("Efgasrdfhegsg",starttime.toString())
            Log.d("Efgasrdfhegsg",endtime.toString())
            Log.d("Efgasrdfhegsg",currtime.toString())

            if (endtime > currtime && currtime > starttime){

                holder.constraintLayout2.visibility = View.VISIBLE

                val timer = object: CountDownTimer((endtime-currtime)*60000, 30000) {
                    override fun onTick(milisecond: Long) {
                        var timegone = TimeUnit.MILLISECONDS.toMinutes(milisecond)

                        var progress = (timegone * 100)/ progressbarlength

                        holder.progressView1.progress = 100 - progress.toFloat()
                        holder.progressView1.labelText = "${timegone.toString()} minuts left"

                        Log.d("Efgasegsg",currtime.toString())
                        Log.d("Efgasegsg",timegone.toString())
                        Log.d("Efgasegsg",progress.toString())
                        Log.d("Efgasegsg",progressbarlength.toString())

                        if (progress.toFloat().toInt() == 100){
                            holder.progressView1.labelText = "Competition is Over"
                        }
                    }
                    override fun onFinish() {

                    }
                }
                timer.start()

            }else if(endtime < currtime){
                holder.progressView1.labelText = "Competition is over"
                holder.progressView1.labelGravity = Gravity.CENTER
                holder.progressView1.progress = 100f
            }else if(starttime > currtime){
                holder.progressView1.labelText = "Competition is Yet to start"
                holder.progressView1.labelGravity = Gravity.CENTER
                holder.progressView1.progress = 0f
            }
        }

        holder.categoriitem.setOnClickListener {
            var intent = Intent(context, Main_video_list::class.java)
            competitionname = datalist[position].competition_name
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    class compitionholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var categori_name: TextView = itemView.findViewById(R.id.categori_name)
        var logo: ImageView = itemView.findViewById(R.id.logo)
        var categoriitem: CardView = itemView.findViewById(R.id.categoriitem)
        var progressView1: ProgressView = itemView.findViewById(R.id.progressView1)
        var constraintLayout2: ConstraintLayout = itemView.findViewById(R.id.constraintLayout2)
        var MKLoader: MKLoader = itemView.findViewById(R.id.MKLoader)

    }
}