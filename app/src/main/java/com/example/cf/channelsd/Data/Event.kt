package com.example.cf.channelsd.Data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.parceler.Parcel
@Parcel

data class Event(
        val username: String,
        @SerializedName("event_name")
        val eventName: String,
        @SerializedName("description")
        val eventDescription: String,
        val prize: String,
        @SerializedName("date_event")
        val eventDate: String,
        @SerializedName("contestant1_name")
        val eventContestant1: String,
        @SerializedName("contestant2_name")
        val eventContestant2: String,
        @SerializedName("creator_name")
        val eventCommentator: String,
        @SerializedName("id")
        val eventId : Int,
        @SerializedName("event_image")
        val eventImage : String
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(eventName)
        parcel.writeString(eventDescription)
        parcel.writeString(prize)
        parcel.writeString(eventDate)
        parcel.writeString(eventContestant1)
        parcel.writeString(eventContestant2)
        parcel.writeString(eventCommentator)
        parcel.writeInt(eventId)
        parcel.writeString(eventImage)
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