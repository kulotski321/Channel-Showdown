package com.example.cf.channelsd.Data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Key(
        @SerializedName("api_key")
        val apiKey : String,
        @SerializedName("session_id")
        val sessionId : String,
        @SerializedName("token")
        val token : String
)
