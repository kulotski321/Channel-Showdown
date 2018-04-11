package com.example.cf.channelsd.Activities


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.text.TimeZoneNames
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import kotlinx.android.synthetic.main.acitivity_create_event.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.*


class CreateEventActivity : AppCompatActivity() {
    private var eventInterface: EventInterface = ApiUtils.apiEvent
    private var formatDateTime = DateFormat.getDateTimeInstance()
    private var dateTime = Calendar.getInstance()
    private var realDate : String = ""
    private var realTime : String = ""
    // for date
    private var rYear : Int ?= null
    private var rMonth : Int ?= null
    private var rDay : Int ?= null
    // for time
    private var rHour : Int ?= null
    private var rMinute: Int ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_create_event)
        val timezoneID : String? = TimeZone.getDefault().id
        Log.e("TIMEZONE", timezoneID)
        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = preferences.edit()
        confirm_event_btn.setOnClickListener {
            if (checkTextFields() == 3 && realDate != "" && realTime != "") {
                val eventName = input_event_name.text.toString()
                val eventDateTime = "$realDate $realTime"
                val timeZone : String = TimeZone.getDefault().id
                val eventPrize = input_event_prize.text.toString()
                val eventDescription = input_event_description.text.toString()
                val username = preferences.getString("username_pref", "")
                sendEvent(username,eventName,eventDescription,eventPrize,eventDateTime,timeZone)
            }
        }
        datePicker_btn.setOnClickListener {
            updateDate()

        }
        timePicker_btn.setOnClickListener {
            updateTime()
        }
        updateTextLabel()
        //Log.e("DATE TIME",)
    }

    fun toastMessage(message: String) {
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

    private fun editTextLength(editText: EditText): Int {
        return editText.text.toString().length
    }

    private fun popUpError(message: String, editText: EditText) {
        editText.error = message
    }

    private fun checkTextFields(): Int {
        var checked = 0
        if (editTextLength(input_event_name) > 0) {
            checked++
        } else {
            popUpError("This field cannot be blank.", input_event_name)
        }
        if (editTextLength(input_event_prize) > 0) {
            checked++
        } else {
            popUpError("This field cannot be blank.", input_event_prize)
        }
        if (editTextLength(input_event_description) > 0) {
            checked++
        } else {
            popUpError("This field cannot be blank.", input_event_description)
        }
        return checked
    }

    private fun sendEvent(username: String, eventName: String, eventDescription: String, eventPrize: String, eventDate: String,timezone: String) {
        eventInterface.createEvent(username, eventName, eventDescription, eventPrize, eventDate,timezone).enqueue(object : Callback<Event> {
            override fun onFailure(call: Call<Event>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
            }

            override fun onResponse(call: Call<Event>?, response: Response<Event>?) {
                Log.e("response: ", response?.body().toString())
                if (response!!.isSuccessful) {
                    toastMessage("Create Successful")
                    finish()
                    overridePendingTransition(0, 0)
                } else {
                    toastMessage("You already have an upcoming event")
                }
            }
        })
    }

    private fun updateDate() {
        DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateTime() {
        TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show()
    }

    var d: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        dateTime.set(Calendar.YEAR, year)
        dateTime.set(Calendar.MONTH, monthOfYear)
        dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        rYear = year
        rMonth = monthOfYear + 1
        rDay = dayOfMonth
        val strDate: String = if(rMonth!! < 10){
            if(rDay!! < 10){
                "$rYear-0$rMonth-0$rDay"
            }else{
                "$rYear-0$rMonth-$rDay"
            }
        }else{
            if(rDay!! < 10){
                "$rYear-$rMonth-0$rDay"
            }else{
                "$rYear-$rMonth-$rDay"
            }
        }
        Log.e("DATE: ",strDate)
        realDate = strDate
        updateTextLabel()
    }

    var t: TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dateTime.set(Calendar.MINUTE, minute)
        rHour = hourOfDay
        rMinute = minute
        val strTime: String = if (rHour!! < 10){
            if(rMinute!! < 10){
                "0$rHour:0$rMinute:00"
            }else{
                "0$rHour:$rMinute:00"
            }
        }else{
            if(rMinute!! < 10){
                "$rHour:0$rMinute:00"
            }else{
                "$rHour:$rMinute:00"
            }
        }
        Log.e("TIME: ",strTime)
        realTime = strTime
        updateTextLabel()
    }

    private fun updateTextLabel() {
        textDateTime.text = formatDateTime.format(dateTime.time)
    }
}