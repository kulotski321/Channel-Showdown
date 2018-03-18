package com.example.cf.channelsd.Interfaces


import com.example.cf.channelsd.Data.User
import retrofit2.Call;
import retrofit2.http.*

interface RegisterInterface {
    @FormUrlEncoded
    @POST("/user/registration/")
    fun createUserInfo(@Field("username") username: String,
                       @Field("email")email: String,
                       @Field("password")password: String,
                       @Field("userType")userType: String): Call<User>
}