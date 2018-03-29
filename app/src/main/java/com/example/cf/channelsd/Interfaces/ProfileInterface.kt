package com.example.cf.channelsd.Interfaces

import com.example.cf.channelsd.Data.Reply
import com.example.cf.channelsd.Data.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import com.google.gson.JsonObject
import retrofit2.http.POST
import retrofit2.http.Multipart



interface ProfileInterface {
    @FormUrlEncoded
    @POST("/user/edituser/")
    fun sendAdditionalInfo(@Field("username") username: String,
                           @Field("firstName") firstName: String,
                           @Field("lastName") lastName: String,
                           @Field("bio") bio:String): Call<User>

    @FormUrlEncoded
    @POST("/user/userprofile/")
    fun viewProfileApplicant(@Field("username")username: String): Call<User>

    @Multipart
    @POST("/user/uploadprofpic/")
    fun uploadPhoto(@Part("username") username: RequestBody,
                    @Part image: MultipartBody.Part): Call<Reply>

    @Multipart
    @POST("/user/uploadthumbnail/")
    fun uploadThumbNail(@Part("username") username: RequestBody,
                        @Part image: MultipartBody.Part): Call<Reply>

    @Multipart
    @POST("/user/uploadvideo/")
    fun uploadVideoProfile(@Part("username") username: RequestBody,
                           @Part profileVideo: MultipartBody.Part): Call<Reply>


}