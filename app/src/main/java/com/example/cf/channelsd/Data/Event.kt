package com.example.cf.channelsd.Data

import android.os.Parcelable
import org.parceler.Parcel


@Parcel
data class Event(
        val username: String,
        val eventName: String,
        val eventDescription: String,
        val prize: String,
        val eventDate: String,
        val eventStatus: Int
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(eventName)
        parcel.writeString(eventDescription)
        parcel.writeString(prize)
        parcel.writeString(eventDate)
        parcel.writeInt(eventStatus)
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