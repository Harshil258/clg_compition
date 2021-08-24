package com.webninjas.clgcompititionadmin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.webninjas.clgcompititionadmin.pref.MOBILE_NO
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val ADMIN_CHANNEL_ID = MOBILE_NO

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("egwgdgrhseg", remoteMessage.toString())
        Log.d("egwgdgrhseg", remoteMessage.data.toString())
        val imageUrl: String = remoteMessage.data.get("logourl").toString()

        sendNotification(
            remoteMessage.data["title"].toString(),
            remoteMessage.data["message"].toString(),
            getBitmapfromUrl(imageUrl).toString()
        )
    }

    private fun sendNotification(title: String, massage: String, url: String) {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)

        val intent = Intent(this, compition_list::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.mipmap.ic_launcher
        )

        val notiStyle = NotificationCompat.BigPictureStyle()
        notiStyle.setSummaryText(massage)
        notiStyle.bigPicture(getBitmapfromUrl(url))

        val notiStyle1 = NotificationCompat.BigPictureStyle()
        notiStyle1.setSummaryText(massage)
        notiStyle1.bigPicture(
            BitmapFactory.decodeResource(
                resources,
                R.mipmap.ic_launcher_foreground
            )
        )

        if (url != "") {
            val notificationSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            var notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(massage)
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.color = resources.getColor(R.color.black)
            }
            notificationManager.notify(notificationID, notificationBuilder.build())
        } else {
            val notificationSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            var notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(massage)
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)
                .setStyle(notiStyle1)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.color = resources.getColor(R.color.black)
            }
            notificationManager.notify(notificationID, notificationBuilder.build())

        }


    }

    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName: CharSequence = "New notification"
        val adminChannelDescription = "Device to device notification"
        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }
}