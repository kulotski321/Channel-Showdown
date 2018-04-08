package com.example.cf.channelsd.Interfaces

import com.example.cf.channelsd.Data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface EventInterface {
    @FormUrlEncoded
    @POST("/event/createevent/")
    fun createEvent(@Field("username") username: String,
                    @Field("eventName") eventName: String,
                    @Field("eventDescription") eventDescription: String,
                    @Field("prize") prize: String,
                    @Field("eventDate") eventDate: String,
                    @Field("timezone")timeZone: String): Call<Event>

    @FormUrlEncoded
    @POST("/event/upcomingevents/")
    fun getUpcomingEventList(@Field("timezone")timezone: String) : Call<UpcomingEventList>

    @FormUrlEncoded
    @POST("/event/creatoreventprofile/")
    fun getMyEvent(@Field("username") username:String,
                   @Field("timezone") timezone: String) : Call<UpcomingEvent>

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

    @Multipart
    @POST("/event/uploadeventimage/")
    fun uploadEventPicture(@Part("event_id") eventId: RequestBody,
                           @Part profileVideo: MultipartBody.Part): Call<Reply>

    @FormUrlEncoded
    @POST("/event/myevent/")
    fun getAcceptedEvent(@Field("username") username: String,
                         @Field("timezone")timezone: String) : Call<UpcomingEvent>

    @FormUrlEncoded
    @POST("/livestream/gettokenpublisher/")
    fun getKeyContestant(@Field("username") username: String,
                         @Field("event_id") eventId: Int) : Call<Key>

    @FormUrlEncoded
    @POST("/livestream/startlivestream/")
    fun getKeyCommentator(@Field("username") username: String,
                         @Field("event_id") eventId: Int) : Call<Key>

}