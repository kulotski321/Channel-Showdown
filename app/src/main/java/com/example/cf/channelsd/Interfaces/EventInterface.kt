package com.example.cf.channelsd.Interfaces

import com.example.cf.channelsd.Data.*
import retrofit2.Call
import retrofit2.http.*

interface EventInterface {
    @FormUrlEncoded
    @POST("/event/createevent/")
    fun createEvent(@Field("username") username: String,
                    @Field("eventName") eventName: String,
                    @Field("eventDescription") eventDescription: String,
                    @Field("prize") prize: String,
                    @Field("eventDate") eventDate: String): Call<Event>

    @GET("/event/upcomingevents/")
    fun getUpcomingEventList() : Call<UpcomingEventList>

    @FormUrlEncoded
    @POST("/event/creatoreventprofile/")
    fun getMyEvent(@Field("username") username:String):Call<UpcomingEvent>

    @FormUrlEncoded
    @POST("/event/sendentry/")
    fun sendEntry(@Field("username") username: String,
                  @Field("event_id") eventId: Int): Call<Entry>

    @FormUrlEncoded
    @POST("/event/allentries/")
    fun getEntries(@Field("event_id") eventId: Int): Call<EntryList>

    @FormUrlEncoded
    @POST("/event/approveentry/")
    fun acceptApplicant(@Field("username") username: String,
                        @Field("entry_id") eventId: Int): Call<Reply>

    @FormUrlEncoded
    @POST("/event/rejectentry/")
    fun rejectApplicant(@Field("username") username: String,
                        @Field("entry_id") eventId: Int): Call<Reply>

}