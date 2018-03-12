package com.example.cf.channelsd.Data

import android.os.Parcelable
import org.parceler.Parcel

@Parcel
data class User(
        var session_key: String,
        var username: String,
        val email: String,
        val password: String,
        val userType: String) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
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
        parcel.writeString(password)
        parcel.writeString(userType)
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