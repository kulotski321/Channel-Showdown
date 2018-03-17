package com.example.cf.channelsd.Interfaces

import android.util.EventLog
import com.example.cf.channelsd.Data.Event
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by CF on 3/15/2018.
 */
interface EventInterface {
    @POST("/text/")
    @FormUrlEncoded
    fun createEvent(@Field("username")username: String,
                    @Field("eventName")eventName: String,
                    @Field("eventDescription")eventDescription: String,
                    @Field("prize")prize: String,
                    @Field("eventDate")eventDate: String): Call<Event>
}