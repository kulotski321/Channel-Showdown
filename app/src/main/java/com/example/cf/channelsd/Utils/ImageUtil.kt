package com.example.cf.channelsd.Utils

import android.media.MediaMetadataRetriever
import android.os.Build
import android.graphics.Bitmap

object ImageUtil{
    fun retriveVideoFrameFromVideo(videoPath: String): Bitmap? {
        val bitmap: Bitmap
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 16)
                mediaMetadataRetriever.setDataSource(videoPath, HashMap())
            else
                mediaMetadataRetriever.setDataSource(videoPath)

            bitmap = mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release()
            }
        }
        return bitmap
    }
}
