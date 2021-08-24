package com.webninjas.clgcompititionadmin

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessaging
import com.webninjas.clgcompititionadmin.pref.MOBILE_NO


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */
        val token = FirebaseInstanceId.getInstance().token

        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO)
        Log.i(
            TAG,
            "onTokenRefresh completed with token: $token"
        )
    }

    companion object {
        val TAG = "mFirebaseIIDService"
        val SUBSCRIBE_TO = MOBILE_NO
    }
}