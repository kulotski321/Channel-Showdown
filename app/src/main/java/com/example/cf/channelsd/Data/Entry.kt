package com.example.cf.channelsd.Data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Entry (
        val username: String,
        @SerializedName("id")
        val entryId: Int,
        @SerializedName("entry_status")
        val status: Int
) {

}