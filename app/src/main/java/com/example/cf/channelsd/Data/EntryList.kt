package com.example.cf.channelsd.Data

import com.google.gson.annotations.SerializedName

/**
 * Created by CF on 3/22/2018.
 */
class EntryList {
        @SerializedName("entries")
        val entries: ArrayList<Entry> ?= null
}