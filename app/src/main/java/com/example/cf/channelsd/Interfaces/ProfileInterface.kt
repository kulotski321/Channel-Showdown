package com.example.cf.channelsd.Interfaces

import com.example.cf.channelsd.Data.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ProfileInterface {
    @FormUrlEncoded
    @POST("/user/edituser/")
    fun sendAdditionalInfo(@Field("username")username: String,
                           @Field("firstName")firstName: String,
                           @Field("lastName")lastName: String,
                           @Field("bio")bio:String): Call<User>

    @FormUrlEncoded
    @POST("/user/userprofile/")
    fun viewProfileApplicant(@Field("username")username: String): Call<User>
}