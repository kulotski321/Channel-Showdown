package com.example.cf.channelsd.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.UpcomingEvent
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
    var dateFormat = DateFormat.getDateTimeInstance()
    var dateTime = Calendar.getInstance()!!
    private var remainingTime:Long = 999999
    private var eventDateTime: Long = 0
    var seconds : Long = 0
    var minutes : Long = 0
    var hours : Long = 0
    lateinit var mainTimer : TextView
    lateinit var streamButton : Button
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_events, container, false)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var timer : TextView = getView()!!.findViewById(R.id.myevent_time)
        var button : Button = getView()!!.findViewById(R.id.myevent_stream_btn)
        mainTimer = timer
        streamButton = button
        val preferences: SharedPreferences = this.activity!!.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        val timeZone : String = TimeZone.getDefault().id
        val username = preferences.getString("username_pref", "")
        getAcceptedEvent(username,timeZone)

        nomyevent_img.setOnClickListener{
            toastMessage("You currently have no accepted events")
        }
        myevent_stream_btn.setOnClickListener {
            if (remainingTime >= 0){
                toastMessage("Event hasn't started yet")
            }else{

            }
        }
    }
    private fun getAcceptedEvent(username : String, timezone : String){
        eventInterface.getAcceptedEvent(username,timezone).enqueue(object : Callback<UpcomingEvent>{
            override fun onFailure(call: Call<UpcomingEvent>?, t: Throwable?) {
                if(t?.message == "unexpected end of stream"){
                    getAcceptedEvent(username,timezone)
                }
            }

            override fun onResponse(call: Call<UpcomingEvent>?, response: Response<UpcomingEvent>?) {
                if(response!!.isSuccessful){
                    nomyevent_img.visibility = View.INVISIBLE
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

                    Log.e("EVENT DATE TIME:",eventDateTime.toString())
                }else{
                    nomyevent_img.visibility = View.VISIBLE
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
                var today = Calendar.getInstance()!!
                remainingTime = eventDateTime - today.timeInMillis
                if(remainingTime > 0){
                    seconds = (remainingTime / 1000) % 60
                    minutes = (remainingTime / (1000 * 60) % 60)
                    hours = (remainingTime / (1000 * 60 * 60))
                    Log.e("SECONDS:",seconds.toString())
                    Log.e("MINUTES:",minutes.toString())
                    Log.e("HOURS:",hours.toString())
                    mainTimer.text = "$hours hrs $minutes mins $seconds secs"
                    streamButton.setBackgroundColor(Color.RED)

                }else{
                    mainTimer.text = "0"
                    streamButton.setBackgroundColor(Color.GREEN)
                }
                remainingTime-=1000
                Log.e("today:",today.timeInMillis.toString())
                Log.e("remaining time:",remainingTime.toString())
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

