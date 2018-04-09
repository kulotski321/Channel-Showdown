package com.example.cf.channelsd.Retrofit

import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class ProgressRequestBody(val context : Context, private val mFile : File, private val mListener: UploadCallbacks,private val selectedVideo : Uri) : RequestBody() {


    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Long)
        fun onError()
        fun onFinish()
    }

    override fun contentType(): MediaType?{
        // i want to upload only videos
        return MediaType.parse(context.contentResolver.getType(selectedVideo))
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val inputStream = FileInputStream(mFile)
        var uploaded: Long = 0

        inputStream.use {
            do{
                val read: Int = inputStream.read(buffer)

                if(read == -1){
                    break
                }
                mListener.onProgressUpdate(((100 * uploaded / fileLength)))
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }while (true)
        }
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) : Runnable {

        override fun run() {
            mListener.onProgressUpdate((100 * mUploaded / mTotal))
        }
    }

    companion object {

        private const val DEFAULT_BUFFER_SIZE = 4096
    }

}