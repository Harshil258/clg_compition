package com.webninjas.clgcompititionadmin

import java.net.HttpURLConnection
import java.net.URL
import android.util.Log
import java.io.*
import java.lang.StringBuilder
import java.net.URLEncoder


class delete {

    companion object {
        const val UPLOAD_URL = "https://wallpaperadmin007.000webhostapp.com/VideoUpload/delete.php"
    }

    fun deletevideo(url1 : String) : String?{

        var conn: HttpURLConnection? = null
        val url = URL(UPLOAD_URL)
        conn = url.openConnection() as HttpURLConnection?
        conn?.setDoOutput(true)
        conn?.setDoInput(true);

        var url12  = url1
        var serverResponseCode : Int

        Log.d("egedsgsg",url12.substringAfter("uploads/"))

        val wr = DataOutputStream(
            conn!!.outputStream
        )
        wr.writeBytes("filename=${url12.substringAfter("uploads/")}")
        serverResponseCode = conn.getResponseCode()
        wr.flush()
        wr.close()



        return if (serverResponseCode == 200) {
            val sb = StringBuilder()
            try {
                val `is` = conn.inputStream
                val rd = BufferedReader(InputStreamReader(`is`))
                var line: String?
                val response = StringBuffer()
                while (rd.readLine().also { line = it } != null) {
                    response.append(line)
                    response.append('\r')
                    sb.append(line)
                }
                rd.close()
//                return response.toString()
            } catch (ioex: IOException) {
            }
            sb.toString()
        } else {
            "Could not delete"
        }

    }


}