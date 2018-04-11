package com.example.cf.channelsd.Data

import com.google.gson.annotations.SerializedName

data class Result(
        @SerializedName("contestant1_name")
        val contestant1: String,
        @SerializedName("contestant2_name")
        val contestant2: String,
        @SerializedName("contestant1_votes")
        val votes1: Int,
        @SerializedName("contestant2_votes")
        val votes2: Int,
        @SerializedName("contestant1_image")
        val contestant1Image : String,
        @SerializedName("contestant2_image")
        val contestant2Image : String
)