package com.example.cf.channelsd.Data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ServerResponse (
        @SerializedName("status")
        val reply: String
) {

}