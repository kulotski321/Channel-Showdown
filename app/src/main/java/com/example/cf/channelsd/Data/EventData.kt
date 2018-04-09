package com.example.cf.channelsd.Data

import com.google.gson.annotations.SerializedName


class EventData {

    @SerializedName("event")
    val myEvent : Event ?= null
}