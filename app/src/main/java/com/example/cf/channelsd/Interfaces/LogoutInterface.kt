package com.example.cf.channelsd.Interfaces

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface LogoutInterface {
    @POST("/user/logout/")
    fun logout(@Header("session_key")session_key: String): Call<String>
}