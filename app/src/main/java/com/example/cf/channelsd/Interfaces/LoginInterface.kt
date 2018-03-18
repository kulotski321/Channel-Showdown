package com.example.cf.channelsd.Interfaces

import com.example.cf.channelsd.Data.User
import retrofit2.Call;
import retrofit2.http.*

interface LoginInterface {
    @FormUrlEncoded
    @POST("/user/login/")
    fun sendUserInfo(@Field("username") username: String,
                     @Field("password") password: String): Call<User>
}