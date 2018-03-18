package com.example.cf.channelsd.Data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

data class Event(
        val username: String,
        val eventName: String,
        val eventDescription: String,
        val prize: String,
        val eventDate: String
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(eventName)
        parcel.writeString(eventDescription)
        parcel.writeString(prize)
        parcel.writeString(eventDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: android.os.Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}