package com.webninjas.clgcompition

import android.util.Log
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class Upload {
    private var serverResponseCode = 0
    fun uploadVideo(file: String): String? {
        var conn: HttpURLConnection? = null
        var dos: DataOutputStream? = null
        val lineEnd = "\r\n"
        val twoHyphens = "--"
        val boundary = "*****"
        var bytesRead: Int
        var bytesAvailable: Int
        var bufferSize: Int
        val buffer: ByteArray
        val maxBufferSize = 1 * 1024 * 1024
        val sourceFile = File(file)
        if (!sourceFile.isFile()) {
            Log.e("Huzza", "Source File Does not exist")
            return null
        }
        try {
            val fileInputStream = FileInputStream(sourceFile)
            val url = URL(UPLOAD_URL)
            conn = url.openConnection() as HttpURLConnection
            conn.setDoInput(true)
            conn.setDoOutput(true)
            conn.setUseCaches(false)
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Connection", "Keep-Alive")
            conn.setRequestProperty("ENCTYPE", "multipart/form-data")
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
            conn.setRequestProperty("myFile", file)
            dos = DataOutputStream(conn.getOutputStream())
            dos.writeBytes(twoHyphens + boundary + lineEnd)
            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"$file\"$lineEnd")
            dos.writeBytes(lineEnd)
            bytesAvailable = fileInputStream.available()
            Log.i("Huzza", "Initial .available : $bytesAvailable")
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            buffer = ByteArray(bufferSize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize)
                bytesAvailable = fileInputStream.available()
                bufferSize = Math.min(bytesAvailable, maxBufferSize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            }
            dos.writeBytes(lineEnd)
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
            serverResponseCode = conn.getResponseCode()
            Log.d("SRRgargre",serverResponseCode.toString())
            fileInputStream.close()
            dos.flush()
            dos.close()
        } catch (ex: MalformedURLException) {
            ex.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (serverResponseCode == 200) {
            val sb = StringBuilder()
            try {
                val rd = BufferedReader(
                    InputStreamReader(
                        conn?.getInputStream()
                    )
                )


                var line: String?
                while (rd.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                rd.close()
            } catch (ioex: IOException) {
            }
            sb.toString()
        } else {
            "Could not upload"
        }
    }

    companion object {
        const val UPLOAD_URL = "https://wallpaperadmin007.000webhostapp.com/VideoUpload/upload.php"
    }
}