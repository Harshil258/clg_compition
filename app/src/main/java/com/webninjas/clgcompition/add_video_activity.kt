package com.webninjas.clgcompition

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.deepakkumardk.videopickerlib.EasyVideoPicker
import com.deepakkumardk.videopickerlib.model.SelectionMode
import com.deepakkumardk.videopickerlib.model.SelectionStyle
import com.deepakkumardk.videopickerlib.model.VideoPickerItem
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.skydoves.progressview.ProgressView
import com.webninjas.clgcompition.pref.MOBILE_NO
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.potyvideo.library.AndExoPlayerView

class add_video_activity : AppCompatActivity() {

    lateinit var upload: TextView
    lateinit var choosevideo: TextView
    var imagepath: String = ""
    lateinit var andExoPlayerView: AndExoPlayerView
    lateinit var layout: LinearLayout
    lateinit var db: FirebaseFirestore
    lateinit var progressView: ProgressView
    var videoname = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_video)

        upload = findViewById(R.id.upload)
        choosevideo = findViewById(R.id.choosevideo)
        layout = findViewById(R.id.layout)
        progressView = findViewById(R.id.progressView)
        andExoPlayerView = findViewById<AndExoPlayerView>(R.id.andExoPlayerView)

        Log.d("Agagsg", pref.competitionname.toString())

        choosevideo.setOnClickListener {

            val item = VideoPickerItem().apply {
                showIcon = true
                debugMode = true
//                themeResId = R.style.CustomTheme
                timeLimit =
                    TimeUnit.MINUTES.toMillis(1)   //(Long) max time of video in milliseconds
                selectionMode = SelectionMode.Single  //Other modes are Single & Custom(limit:Int)
                gridDecoration = Triple(2, 20, true)    //(spanCount,spacing,includeEdge)
                limitMessage = "Please select only one pictures"
                showDuration = true
                selectionStyle = SelectionStyle.Large
            }
            EasyVideoPicker().startPickerForResult(this, item, 3000)
        }

        upload.setOnClickListener {
            if (imagepath != "") {
                Log.d("ESdgsergsrgh", imagepath)
                uploadvideo(imagepath)

            } else {
                Toast.makeText(this, "Please select video", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadvideo(imagepath: String) {

        if (imagepath != null) {
            progressView.visibility = View.VISIBLE
            var file = Uri.fromFile(File(imagepath))
            videoname = "${System.currentTimeMillis()}.mp4"
            var reference: StorageReference = FirebaseStorage.getInstance()
                .getReference("Video/${videoname}")

            reference.putFile(file)
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot?> {
                    reference.downloadUrl
                        .addOnSuccessListener(OnSuccessListener<Uri> { uri -> //now sUrl contains downloadURL
                            var sUrl = uri.toString()
                            Log.d("ESdgsergsrgh", sUrl)
                            db = FirebaseFirestore.getInstance()

                            val sdf = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
                            val currentDateandTime: String = sdf.format(Date())
                            val data = hashMapOf(
                                "number" to MOBILE_NO,
                                "timestamp" to currentDateandTime,
                                "url" to sUrl,
                                "compititionname" to pref.competitionname,
                                "videoname" to videoname
                            )

                            Log.d("Agagsg", pref.competitionname.toString())

                            db.collection("videos").document()
                                .set(data)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        progressView.visibility = View.GONE
                                        Toast.makeText(
                                            this,
                                            "Video Has Been Upload Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        this.imagepath = ""
                                        finish()
                                    }
                                }
                        }).addOnFailureListener(OnFailureListener {
                            progressView.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Something Went Wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                }).addOnProgressListener {
                    val progress: Double =
                        100.0 * it.getBytesTransferred() / it.getTotalByteCount()
                    progressView.progress = progress.toFloat()
                    progressView.labelText = "${progress.toInt()}%"
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 3000) {
            val list = EasyVideoPicker.getSelectedVideos(data)  //ArrayList<VideoModel>
            choosevideo.visibility = View.GONE
            imagepath = list?.get(0)?.videoPath.toString()

            layout.visibility = View.VISIBLE

            andExoPlayerView.setSource(imagepath)
        }
    }

    override fun onPause() {
        super.onPause()
        andExoPlayerView.stopPlayer()
    }

    override fun onResume() {
        super.onResume()
        andExoPlayerView.startPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        andExoPlayerView.releasePlayer()
    }



}