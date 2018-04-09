package com.example.cf.channelsd.Data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ServerResponse (
        @SerializedName("status")
        val reply: String
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(reply)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ServerResponse> {
        override fun createFromParcel(parcel: Parcel): ServerResponse {
            return ServerResponse(parcel)
        }

        override fun newArray(size: Int): Array<ServerResponse?> {
            return arrayOfNulls(size)
        }
    }
}