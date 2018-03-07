package com.example.cf.channelsd.Interfaces


import com.example.cf.channelsd.Data.Key
import com.example.cf.channelsd.Data.Post
import retrofit2.Call;
import retrofit2.http.*


interface APIService {
    // Send data
    @POST("/posts")
    @FormUrlEncoded
    fun savePost(@Field("title") title: String,
                 @Field("body") body: String,
                 @Field("userId") userId: Long): Call<Post>

    // Retrieves session ID
    @GET("/livestream/startlivestream/")
    fun getSessionID(): retrofit2.Call<Key>
}