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

) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeString(profilePic)
        parcel.writeString(profileVideo)
        parcel.writeString(thumbNail)
        parcel.writeString(eventImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reply> {
        override fun createFromParcel(parcel: android.os.Parcel): Reply {
            return Reply(parcel)
        }

        override fun newArray(size: Int): Array<Reply?> {
            return arrayOfNulls(size)
        }
    }

}