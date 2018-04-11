package com.example.cf.channelsd.Interfaces

import com.example.cf.channelsd.Data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface EventInterface {
    @FormUrlEncoded
    @POST("/event/createevent/")
    fun createEvent(@Field("username") username: String,
                    @Field("eventName") eventName: String,
                    @Field("eventDescription") eventDescription: String,
                    @Field("prize") prize: String,
                    @Field("eventDate") eventDate: String,
                    @Field("timezone") timeZone: String): Call<Event>

    @FormUrlEncoded
    @POST("/event/upcomingevents/")
    fun getUpcomingEventList(@Field("timezone") timezone: String) : Call<EventDataList>

    @FormUrlEncoded
    @POST("/event/ongoingevents/")
    fun getLiveEventList(@Field("timezone") timezone: String) : Call<EventDataList>

    @FormUrlEncoded
    @POST("/event/history/")
    fun getHistoryList(@Field("username") username: String,
                       @Field("timezone") timezone: String) : Call<EventDataList>

    @FormUrlEncoded
    @POST("/event/finishedevents/")
    fun getEpisodeList(@Field("username") username: String,
                       @Field("timezone") timezone: String) : Call<EventDataList>

    @FormUrlEncoded
    @POST("/event/creatoreventprofile/")
    fun getMyEvent(@Field("username") username:String,
                   @Field("timezone") timezone: String) : Call<EventData>

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
                         @Field("timezone") timezone: String) : Call<EventData>

    @FormUrlEncoded
    @POST("/livestream/gettokenpublisher/")
    fun getKeyContestant(@Field("username") username: String,
                         @Field("event_id") eventId: Int) : Call<Key>

    @FormUrlEncoded
    @POST("/livestream/startlivestream/")
    fun getKeyCommentator(@Field("username") username: String,
                         @Field("event_id") eventId: Int) : Call<Key>

    @FormUrlEncoded
    @POST("/livestream/gettokensubscriber/")
    fun getKeyAudience(@Field("username") username: String,
                       @Field("event_id") eventId: Int) : Call<Key>

    @FormUrlEncoded
    @POST("/livestream/endevent/")
    fun endEvent(@Field("event_id") eventId: Int) : Call<ServerResponse>

    @FormUrlEncoded
    @POST("/livestream/vote/")
    fun voteContestant(@Field("username") username: String,
                       @Field("contestant") contestant: String,
                       @Field("event_id") eventId : Int): Call<ServerResponse>

    @FormUrlEncoded
    @POST("/livestream/startarchive/")
    fun startArchive(@Field("event_id") eventId: Int) : Call<ServerResponse>

    @FormUrlEncoded
    @POST("/event/result/")
    fun viewResult(@Field("event_id") eventId : Int) : Call<Result>

    @FormUrlEncoded
    @POST("/livestream/savedvideo/")
    fun savedVideo(@Field("event_id")eventId : Int) : Call<Video>
}