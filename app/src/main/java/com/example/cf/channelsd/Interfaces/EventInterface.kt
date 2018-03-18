package com.example.cf.channelsd.Interfaces

import android.util.EventLog
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.Data.UpcomingEventList
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface EventInterface {
    @FormUrlEncoded
    @POST("/event/createevent/")
    fun createEvent(@Field("username")username: String,
                    @Field("eventName")eventName: String,
                    @Field("eventDescription")eventDescription: String,
                    @Field("prize")prize: String,
                    @Field("eventDate")eventDate: String): Call<Event>

    @GET("/event/upcomingevents/")
    fun getUpcomingEventList() : Call<UpcomingEventList>

    /*@GET("/event/upcomingevents/")
    fun getEvents(): Call<List<Event>>*/
}