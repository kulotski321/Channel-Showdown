package com.example.cf.channelsd.Interfaces

import com.example.cf.channelsd.Data.Entry
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.Data.UpcomingEvent
import com.example.cf.channelsd.Data.UpcomingEventList
import retrofit2.Call
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("/event/creatoreventprofile/")
    fun getMyEvent(@Field("username")username:String):Call<UpcomingEvent>

    /*@GET("/text/")
    fun getEntries(): Call<>*/
    @FormUrlEncoded
    @POST("/event/sendentry/")
    fun sendEntry(@Field("username") username: String,
                  @Field("event_id") eventId: Int): Call<Entry>

}