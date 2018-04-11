package com.example.cf.channelsd.Activities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.Data.Key
import com.example.cf.channelsd.Data.ServerResponse
import com.example.cf.channelsd.Data.EventData
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import kotlinx.android.synthetic.main.activity_my_event.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.*


class MyEventActivity : AppCompatActivity() {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    private lateinit var myEventMain: Event
    private var dateFormat = DateFormat.getDateTimeInstance()
    private var dateTime = Calendar.getInstance()!!
    private lateinit var eventId : String
    private lateinit var eventPic: String
    private var remainingTime:Long = 999999
    private var eventDateTime: Long = 0
    private var seconds : Long = 0
    private var minutes : Long = 0
    private var hours : Long = 0
    lateinit var timer : TextView
    lateinit var streamButton : Button
    lateinit var textTimer : TextView
    private var canStream = false
    // Opentok keys
    private var apiKey: String ?= null
    private var sessionId : String ?= null
    private var token : String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_event)
        timer = findViewById(R.id.myevent_commentator_time)
        streamButton = findViewById(R.id.start_event_btn)
        textTimer = findViewById(R.id.time_remaining2)
        val username = intent.getStringExtra("username")
        val timeZone : String = TimeZone.getDefault().id
        getMyEvent(username,timeZone)

        view_entries_btn.setOnClickListener {
            val i = Intent(this,EntriesActivity::class.java)
            i.putExtra("eventId",myEventMain.eventId.toString())
            startActivity(i)
        }
        event_contestant1_upcoming_commentator.setOnClickListener {
            val usernameContestant = event_contestant1_upcoming_commentator.text
            if(usernameContestant != "Empty Slot"){
                val i = Intent(this,ViewProfileActivity::class.java)
                i.putExtra("username",usernameContestant)
                i.putExtra("from_link","true")
                i.putExtra("entry_id","blank")
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
                i.putExtra("entry_id","blank")
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
        start_event_btn.setOnClickListener {
            if (remainingTime >= 0){
                toastMessage("Event hasn't started yet")
            }
            if(canStream){
                getCommentatorKey(username,eventId.toInt())
            }
        }
        end_event_btn.setOnClickListener {
            endEvent(eventId.toInt())
        }
    }

    private fun getMyEvent(username: String,timeZone: String) {
        eventInterface.getMyEvent(username,timeZone).enqueue(object : Callback<EventData>{
            override fun onFailure(call: Call<EventData>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
                if(t?.message == "unexpected end of stream"){
                    getMyEvent(username,timeZone)
                }
            }

            override fun onResponse(call: Call<EventData>?, response: Response<EventData>?) {
                if(response!!.isSuccessful){
                    Log.e("Response:", response.toString())
                    val event : EventData ?= response.body()
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
                        val year : String = myEvent.eventDate.substring(0,4)
                        val month : String = myEvent.eventDate.substring(5,7)
                        val day : String = myEvent.eventDate.substring(8,10)
                        val hour : String = myEvent.eventDate.substring(11,13)
                        val minute : String = myEvent.eventDate.substring(14,16)

                        dateTime.set(Calendar.YEAR,year.toInt())
                        dateTime.set(Calendar.MONTH,month.toInt()-1)
                        dateTime.set(Calendar.DAY_OF_MONTH,day.toInt())
                        dateTime.set(Calendar.HOUR_OF_DAY,hour.toInt())
                        dateTime.set(Calendar.MINUTE,minute.toInt())
                        dateTime.set(Calendar.SECOND,0)
                        picasso.load(ApiUtils.BASE_URL + myEvent.eventImage).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(event_picture)
                        event_date_upcoming_commentator.text = dateFormat.format(dateTime.time)
                        eventDateTime = dateTime.timeInMillis
                    }
                }else{

                }
            }
        })
    }
    private fun getCommentatorKey(username: String, eventId: Int){
        eventInterface.getKeyCommentator(username,eventId).enqueue(object: Callback<Key>{
            override fun onFailure(call: Call<Key>?, t: Throwable?) {
                if(t?.message == "unexpected end of stream"){
                    getCommentatorKey(username,eventId)
                }
            }

            override fun onResponse(call: Call<Key>?, response: Response<Key>?) {
                if(response!!.isSuccessful){
                    val key = response.body()
                    apiKey = key!!.apiKey
                    sessionId = key.sessionId
                    token = key.token
                    toastMessage("Live stream started")
                    val i = Intent(this@MyEventActivity,LiveStreamCommentatorActivity::class.java)
                    i.putExtra("api_key",apiKey)
                    i.putExtra("session_id",sessionId)
                    i.putExtra("token",token)
                    i.putExtra("event_id",eventId.toString())
                    startActivity(i)
                }else{
                    toastMessage("Live stream failed to start")
                }
            }
        })
    }
    private fun endEvent(eventId: Int){
        eventInterface.endEvent(eventId).enqueue(object: Callback<ServerResponse>{
            override fun onFailure(call: Call<ServerResponse>?, t: Throwable?) {
                if(t?.message == "unexpected end of stream"){
                    endEvent(eventId)
                }
            }

            override fun onResponse(call: Call<ServerResponse>?, response: Response<ServerResponse>?) {
                val status = response!!.body()
                if (status != null) {
                    toastMessage(status.reply)
                }
                if(response.isSuccessful){
                    finish()
                }
            }
        })
    }
    fun toastMessage(message: String){
        val toast: Toast = Toast.makeText(this,message,Toast.LENGTH_LONG)
        val toastView : View = toast.view
        val toastMessage : TextView = toastView.findViewById(android.R.id.message)
        toastMessage.textSize = 16F
        toastMessage.setPadding(2,2,2,2)
        toastMessage.setTextColor(Color.parseColor("#790e8b"))
        toastMessage.gravity = Gravity.CENTER
        toastView.setBackgroundColor(Color.YELLOW)
        toastView.setBackgroundResource(R.drawable.round_button1)
        toast.show()
    }

    var h = Handler()
    var delay = 1000 //1 second=1000 millisecond
    var runnable: Runnable ?= null

    override fun onResume() {
        super.onResume()
        val username = intent.getStringExtra("username")
        val timeZone : String = TimeZone.getDefault().id
        getMyEvent(username,timeZone)

        //start handler as activity become visible
        h.postDelayed(object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                //do something
                val today = Calendar.getInstance()!!
                remainingTime = eventDateTime - today.timeInMillis
                if(remainingTime > 0){
                    seconds = (remainingTime / 1000) % 60
                    minutes = (remainingTime / (1000 * 60) % 60)
                    hours = (remainingTime / (1000 * 60 * 60))
                    timer.text = "$hours hrs $minutes mins $seconds secs"
                    textTimer.visibility = View.VISIBLE
                    streamButton.setBackgroundResource(R.drawable.round_button2)
                }else{
                    timer.visibility = View.INVISIBLE
                    textTimer.visibility = View.INVISIBLE
                    streamButton.setBackgroundResource(R.drawable.round_button3)
                    canStream = true
                    h.removeCallbacks(runnable)
                }
                remainingTime-=1000
                runnable = this
                h.postDelayed(runnable, delay.toLong())
            }
        }, delay.toLong())

        super.onResume()
    }

    override fun onPause() {
        h.removeCallbacks(runnable) //stop handler when activity not visible
        super.onPause()
    }

}
