package com.example.cf.channelsd.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Activities.LiveStreamContestantActivity
import com.example.cf.channelsd.Activities.ViewProfileActivity
import com.example.cf.channelsd.Data.Key
import com.example.cf.channelsd.Data.EventData
import com.example.cf.channelsd.Interfaces.EventInterface

import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import kotlinx.android.synthetic.main.fragment_my_events.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.*


class MyEventsFragment : Fragment() {

    private val eventInterface: EventInterface = ApiUtils.apiEvent
    private var eventId : Int ?= null

    // Timezone converter and timer
    var dateFormat = DateFormat.getDateTimeInstance()
    var dateTime = Calendar.getInstance()!!
    private var remainingTime:Long = 999999
    private var eventDateTime: Long = 0
    var seconds : Long = 0
    var minutes : Long = 0
    var hours : Long = 0
    lateinit var mainTimer : TextView
    lateinit var streamButton : Button
    lateinit var textTimer : TextView
    private lateinit var username : String
    var canStream = false
    var contestant: String ?= null

    // Opentok keys
    private var apiKey: String ?= null
    private var sessionId : String ?= null
    private var token : String ?= null

    private var noEventImage : ImageView ?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_events, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timer : TextView = getView()!!.findViewById(R.id.myevent_time)
        val button : Button = getView()!!.findViewById(R.id.myevent_stream_btn)
        val text : TextView = getView()!!.findViewById(R.id.time_remaining)
        noEventImage = getView()!!.findViewById(R.id.nomyevent_img)
        mainTimer = timer
        streamButton = button
        textTimer = text
        val preferences: SharedPreferences = this.activity!!.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        val timeZone : String = TimeZone.getDefault().id
        username = preferences.getString("username_pref", "")
        getAcceptedEvent(username,timeZone)

        nomyevent_img.setOnClickListener{
            toastMessage("You currently have no accepted events")
        }
        myevent_stream_btn.setOnClickListener {
            if (remainingTime >= 0){
                toastMessage("Event hasn't started yet")
            }
            if(canStream){
                getContestantKey(username,eventId!!.toInt())
            }
        }
        myevent_commentator.setOnClickListener {
            val i = Intent(context,ViewProfileActivity::class.java)
            i.putExtra("username",myevent_commentator.text)
            i.putExtra("from_link","true")
            startActivity(i)
        }
    }

    private fun getAcceptedEvent(username : String, timezone : String){
        eventInterface.getAcceptedEvent(username,timezone).enqueue(object : Callback<EventData>{
            override fun onFailure(call: Call<EventData>?, t: Throwable?) {
                if(t?.message == "unexpected end of stream"){
                    getAcceptedEvent(username,timezone)
                }
            }

            override fun onResponse(call: Call<EventData>?, response: Response<EventData>?) {
                if(response!!.isSuccessful){
                    noEventImage!!.visibility = View.INVISIBLE
                    val myEvent = response.body()!!.myEvent
                    myevent_title.text = myEvent?.eventName
                    myevent_commentator.text = myEvent?.eventCommentator
                    myevent_details.text = myEvent?.eventDescription
                    myevent_prize.text = myEvent?.prize
                    eventId = myEvent?.eventId
                    context?.picasso?.load(ApiUtils.BASE_URL + myEvent?.eventImage)?.into(myevent_img)
                    val year : String = myEvent?.eventDate!!.substring(0,4)
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
                    dateTime.set(Calendar.SECOND,0)
                    myevent_date.text = dateFormat.format(dateTime.time)
                    eventDateTime = dateTime.timeInMillis
                    if(myEvent.eventContestant1 == username){
                        contestant = "contestant1"
                    }
                    if(myEvent.eventContestant2 == username){
                        contestant = "contestant2"
                    }
                    Log.e("EVENT DATE TIME:",eventDateTime.toString())
                }else{
                    noEventImage!!.visibility = View.VISIBLE
                    myevent_commentator.visibility = View.INVISIBLE
                    myevent_date.visibility = View.INVISIBLE
                    myevent_details.visibility = View.INVISIBLE
                    myevent_img.visibility = View.INVISIBLE
                    myevent_prize.visibility = View.INVISIBLE
                    myevent_stream_btn.visibility = View.INVISIBLE
                    myevent_title.visibility = View.INVISIBLE
                    commentator_textview.visibility = View.INVISIBLE
                    event_date_textview.visibility = View.INVISIBLE
                    details_textview.visibility = View.INVISIBLE
                    myevent_details.visibility = View.INVISIBLE
                    prize_textview.visibility = View.INVISIBLE
                    myevent_time.visibility = View.INVISIBLE
                    time_remaining.visibility = View.INVISIBLE
                }
            }
        })
    }
    private fun getContestantKey(username: String, eventId: Int){
        eventInterface.getKeyContestant(username,eventId).enqueue(object: Callback<Key>{
            override fun onFailure(call: Call<Key>?, t: Throwable?) {
                if(t?.message == "unexpected end of stream"){
                    getContestantKey(username,eventId)
                }
            }

            override fun onResponse(call: Call<Key>?, response: Response<Key>?) {
                if(response!!.isSuccessful){
                    val key = response.body()
                    apiKey = key!!.apiKey
                    sessionId = key.sessionId
                    token = key.token
                    toastMessage("Live stream started")
                    val i = Intent(context, LiveStreamContestantActivity::class.java)
                    i.putExtra("contestant",contestant)
                    i.putExtra("api_key",apiKey)
                    i.putExtra("session_id",sessionId)
                    i.putExtra("token",token)
                    startActivity(i)
                }else{
                    toastMessage("Commentator has not started event yet")
                }
            }
        })
    }
    fun toastMessage(message: String) {
        Toast.makeText(view!!.context, message, Toast.LENGTH_LONG).show();
    }

    var h = Handler()
    var delay = 1000 //1 second=1000 millisecond, 15*1000=15seconds
    var runnable: Runnable ?= null

    override fun onResume() {
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
                    //Log.e("SECONDS:",seconds.toString())
                    //Log.e("MINUTES:",minutes.toString())
                    //Log.e("HOURS:",hours.toString())
                    mainTimer.text = "$hours hrs $minutes mins $seconds secs"
                    textTimer.visibility = View.VISIBLE
                    streamButton.setBackgroundResource(R.drawable.round_button2)

                }else{
                    canStream = true
                    mainTimer.visibility = View.INVISIBLE
                    textTimer.visibility = View.INVISIBLE
                    streamButton.setBackgroundResource(R.drawable.round_button3)
                    h.removeCallbacks(runnable)

                    Log.e("test:","WHY??")
                }
                remainingTime-=1000
                //Log.e("today:",today.timeInMillis.toString())
                //Log.e("remaining time:",remainingTime.toString())
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

