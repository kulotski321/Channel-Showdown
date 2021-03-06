package com.example.cf.channelsd.Data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class User(
        val session_key: String,
        val username: String,
        val email: String,
        val userType: String,
        val firstName: String,
        val lastName: String,
        val bio: String,
        @SerializedName("profile_pic")
        val profilePicture: String,
        @SerializedName("user_video")
        val profileVideo: String,
        @SerializedName("video_thumbnail")
        val profileThumbNail: String) : Parcelable {
        constructor(parcel: android.os.Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
                parcel.writeString(session_key)
                parcel.writeString(username)
                parcel.writeString(email)
                parcel.writeString(userType)
                parcel.writeString(firstName)
                parcel.writeString(lastName)
                parcel.writeString(bio)
                parcel.writeString(profilePicture)
                parcel.writeString(profileVideo)
                parcel.writeString(profileThumbNail)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<User> {
                override fun createFromParcel(parcel: android.os.Parcel): User {
                        return User(parcel)
                }

                override fun newArray(size: Int): Array<User?> {
                        return arrayOfNulls(size)
                }
        }

}
