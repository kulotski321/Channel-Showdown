package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import com.example.cf.channelsd.Data.ApiUtils
import com.example.cf.channelsd.Data.UpcomingEvent
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_my_event.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyEventActivity : AppCompatActivity() {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_my_event)

        val username = intent.getStringExtra("username")
        getMyEvent(username)
    }

    private fun getMyEvent(username: String) {
        eventInterface.getMyEvent(username).enqueue(object : Callback<UpcomingEvent>{
            override fun onFailure(call: Call<UpcomingEvent>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
                if(t?.message == "unexpected end of stream"){
                    getMyEvent(username)
                }
            }

            override fun onResponse(call: Call<UpcomingEvent>?, response: Response<UpcomingEvent>?) {
                if(response!!.isSuccessful){
                    Log.e("Response:", response.toString())
                    val event : UpcomingEvent ?= response.body()
                    if(event != null){
                        val myEvent = event.myEvent
                        Log.e("Event Details:", myEvent.toString())
                        event_name_upcoming_commentator.text = myEvent?.eventName
                        var contestant1 : String = myEvent!!.eventContestant1
                        var contestant2 : String = myEvent.eventContestant2
                        if(contestant1 == ""){
                            contestant1 = "Empty Slot"
                        }
                        if(contestant2 == ""){
                            contestant2 = "Empty Slot"
                        }
                        //Log.e("contestant",myEvent.eventContestant1)
                        event_contestant1_upcoming_commentator.text = contestant1
                        event_contestant2_upcoming_commentator.text = contestant2
                        event_description_upcoming_commentator.text =myEvent.eventDescription
                        event_prize_upcoming_commentator.text = myEvent.prize
                        event_date_upcoming_commentator.text = myEvent.eventDate
                    }
                }
            }
        })
    }
}