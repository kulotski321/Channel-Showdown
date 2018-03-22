package com.example.cf.channelsd.Data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


class UpcomingEventList {

    @SerializedName("events")
    val events : ArrayList<Event> ?= null

}