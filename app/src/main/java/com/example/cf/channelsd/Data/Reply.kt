package com.example.cf.channelsd.Data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class Reply (
        @SerializedName("status")
        val status: String,
        @SerializedName("profile_pic")
        val profilePic: String,
        @SerializedName("user_video")
        val profileVideo: String,
        @SerializedName("video_thumbnail")
        val thumbNail: String,
        @SerializedName("event_image")
        val eventImage: String

) {

}