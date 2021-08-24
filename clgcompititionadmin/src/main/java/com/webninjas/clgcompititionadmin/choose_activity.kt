package com.webninjas.clgcompititionadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class choose_activity : AppCompatActivity() {

    lateinit var myvideo : TextView
    lateinit var addvideo : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        myvideo = findViewById(R.id.myvideo)
        addvideo = findViewById(R.id.addvideo)

        myvideo.setOnClickListener {
            var intent = Intent(this, my_videos::class.java)
            startActivity(intent)
        }

        addvideo.setOnClickListener {
            var intent = Intent(this, add_video_activity::class.java)
            startActivity(intent)
        }

    }
}