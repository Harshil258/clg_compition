package com.webninjas.clgcompition

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import com.potyvideo.library.AndExoPlayerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class Video_open_Activity : AppCompatActivity() {

    lateinit var layout : LinearLayout
    lateinit var like : ImageView
    var isliked = false
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var videoplayer: VideoView
    lateinit var andExoPlayerView: AndExoPlayerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_open)

        layout = findViewById(R.id.layout)
        like = findViewById(R.id.like)
//        videoplayer = findViewById(R.id.videoplayer)

        var videourl = intent.getStringExtra("videourl").toString()
        var documentid = intent.getStringExtra("documentid").toString()
        var compititionname = intent.getStringExtra("compititionname").toString()
        andExoPlayerView = findViewById<AndExoPlayerView>(R.id.andExoPlayerView)
        andExoPlayerView.setSource(videourl)


        setlikes(compititionname, documentid)

        like.setOnClickListener {
            managelike(compititionname,documentid)
        }


    }

    override fun onPause() {
        super.onPause()
//        videoplayer.pause()
        andExoPlayerView.pausePlayer()
    }

    override fun onResume() {
        super.onResume()
//        videoplayer.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
//        videoplayer.suspend()
    }

    private fun managelike(compititionname: String, documentid : String) {
        Log.d("arhgerherth", like.drawable.toString())
        Log.d("arhgerherth", getDrawable(R.drawable.ic_liked).toString())

        if (isliked) {
            Log.d("arhgerherth", "unliked")
            db.collection("competitions").document(compititionname).collection("Videos")
                .document(documentid)
                .collection("Likes").document(pref.MOBILE_NO).delete()
            isliked = false
            like.setImageDrawable(getDrawable(R.drawable.ic_notliked))
            Toast.makeText(this, "unliked", Toast.LENGTH_SHORT).show()

        } else {
            val current: String =
                SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            var map: MutableMap<String, String> = HashMap()
            map.put("mobile", pref.MOBILE_NO)
            map.put("Time", current)
            db.collection("competitions").document(compititionname).collection("Videos")
                .document(documentid)
                .collection("Likes").document(pref.MOBILE_NO).set(map)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        like.setImageDrawable(getDrawable(R.drawable.ic_liked))
                        isliked = true

                        Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("segsgrrgrg", it.result.toString())
                }
        }
    }

    fun setlikes(compititionname: String, documentid : String) {

        db.collection("competitions").document(compititionname).collection("Videos")
            .document(documentid)
            .collection("Likes").get().addOnCompleteListener {
                Log.d("sgfaerhaerh", "sucess")
                for (document in it.result?.documents!!) {
                    var number = document.id.toString()
                    Log.d("sgfaerhaerh", number)
                    Log.d("sgfaerhaerh", pref.MOBILE_NO)
                    if (pref.MOBILE_NO.toLong() == number.toLong()) {
                        like.setImageDrawable(getDrawable(R.drawable.ic_liked))
                        isliked = true
                        Log.d("arhgertrherth", "liked  " + pref.MOBILE_NO)
                    }
                }
            }
    }
}