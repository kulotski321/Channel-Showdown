package com.example.cf.channelsd.Data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Reply (
        @SerializedName("status")
        val status: String
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reply> {
        override fun createFromParcel(parcel: Parcel): Reply {
            return Reply(parcel)
        }

        override fun newArray(size: Int): Array<Reply?> {
            return arrayOfNulls(size)
        }
    }
}