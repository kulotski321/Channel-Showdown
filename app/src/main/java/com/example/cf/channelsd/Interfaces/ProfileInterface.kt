package com.example.cf.channelsd.Interfaces

import com.example.cf.channelsd.Data.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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

    @Multipart
    @POST("/")
    fun uploadPhoto(@Part image: MultipartBody.Part): Call<RequestBody>


}