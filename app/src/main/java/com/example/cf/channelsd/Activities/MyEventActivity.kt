package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.Data.UpcomingEvent
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import kotlinx.android.synthetic.main.activity_my_event.*
import kotlinx.android.synthetic.main.event_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.*


class MyEventActivity : AppCompatActivity() {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    private lateinit var myEventMain: Event
    var dateFormat = DateFormat.getDateTimeInstance()
    var dateTime = Calendar.getInstance()!!
    private lateinit var eventId : String
    private lateinit var eventPic: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_my_event)

        val username = intent.getStringExtra("username")
        val timeZone : String = TimeZone.getDefault().id

        getMyEvent(username,timeZone)

        view_entries_btn.setOnClickListener {
            val i = Intent(this,EntriesActivity::class.java)
            toastMessage(myEventMain.eventId.toString())
            i.putExtra("eventId",myEventMain.eventId.toString())
            startActivity(i)
        }
        event_contestant1_upcoming_commentator.setOnClickListener {
            val usernameContestant = event_contestant1_upcoming_commentator.text
            if(usernameContestant != "Empty Slot"){
                val i = Intent(this,ViewProfileActivity::class.java)
                i.putExtra("username",usernameContestant)
                i.putExtra("from_link","true")
                startActivity(i)
            }else{
                toastMessage("No contestant yet")
            }
        }
        event_contestant2_upcoming_commentator.setOnClickListener {
            val usernameContestant = event_contestant2_upcoming_commentator.text
            if(usernameContestant != "Empty Slot"){
                val i = Intent(this,ViewProfileActivity::class.java)
                i.putExtra("username",usernameContestant)
                i.putExtra("from_link","true")
                startActivity(i)
            }else{
                toastMessage("No contestant yet")
            }
        }
        upload_myevent_pic_btn.setOnClickListener {
            val i = Intent(this, UploadEventPictureActivity::class.java)
            i.putExtra("event_id",eventId)
            i.putExtra("event_image",eventPic)
            startActivity(i)
        }
    }

    private fun getMyEvent(username: String,timeZone: String) {
        eventInterface.getMyEvent(username,timeZone).enqueue(object : Callback<UpcomingEvent>{
            override fun onFailure(call: Call<UpcomingEvent>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
                if(t?.message == "unexpected end of stream"){
                    getMyEvent(username,timeZone)
                }
            }

            override fun onResponse(call: Call<UpcomingEvent>?, response: Response<UpcomingEvent>?) {
                if(response!!.isSuccessful){
                    Log.e("Response:", response.toString())
                    val event : UpcomingEvent ?= response.body()
                    if(event != null){
                        val myEvent = event.myEvent
                        myEventMain = myEvent!!
                        eventId = myEvent.eventId.toString()
                        eventPic = myEvent.eventImage
                        Log.e("Event Details:", myEvent.toString())
                        event_name_upcoming_commentator.text = myEvent.eventName
                        var contestant1 : String = myEvent.eventContestant1
                        var contestant2 : String = myEvent.eventContestant2
                        if(contestant1 == ""){
                            contestant1 = "Empty Slot"
                        }
                        if(contestant2 == ""){
                            contestant2 = "Empty Slot"
                        }
                        event_contestant1_upcoming_commentator.text = contestant1
                        event_contestant2_upcoming_commentator.text = contestant2
                        event_description_upcoming_commentator.text =myEvent.eventDescription
                        event_prize_upcoming_commentator.text = myEvent.prize
                        //event_date_upcoming_commentator.text = myEvent.eventDate
                        val year : String = myEvent.eventDate.substring(0,4)
                        val month : String = myEvent.eventDate.substring(5,7)
                        val day : String = myEvent.eventDate.substring(8,10)
                        val hour : String = myEvent.eventDate.substring(11,13)
                        val minute : String = myEvent.eventDate.substring(14,16)

                        Log.e("DATE",myEvent.eventDate)
                        Log.e("DATE","$year $month $day $hour $minute")
                        dateTime.set(Calendar.YEAR,year.toInt())
                        dateTime.set(Calendar.MONTH,month.toInt()-1)
                        dateTime.set(Calendar.DAY_OF_MONTH,day.toInt())
                        dateTime.set(Calendar.HOUR_OF_DAY,hour.toInt())
                        dateTime.set(Calendar.MINUTE,minute.toInt())
                        picasso.load(ApiUtils.BASE_URL + myEvent.eventImage).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(event_picture)
                        event_date_upcoming_commentator.text = dateFormat.format(dateTime.time)
                        //event_date_upcoming_commentator.text = dateFormat.format("2018-04-17 10:00:00")
                    }
                }else{

                }
            }
        })
    }
    fun toastMessage(message: String){
        Toast.makeText(this@MyEventActivity, message, Toast.LENGTH_LONG).show();
    }

    override fun onResume() {
        super.onResume()
        val username = intent.getStringExtra("username")
        val timeZone : String = TimeZone.getDefault().id
        getMyEvent(username,timeZone)
    }
}