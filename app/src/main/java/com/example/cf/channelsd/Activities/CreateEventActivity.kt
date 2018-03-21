package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.cf.channelsd.Data.ApiUtils
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.acitivity_create_event.*


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateEventActivity: AppCompatActivity() {
    private var eventInterface: EventInterface ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_create_event)

        eventInterface = ApiUtils.apiEvent
        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = preferences.edit()
        confirm_event_btn.setOnClickListener {
            if(checkTextFields() == 5){
                val eventName = input_event_name.text.toString()
                val eventDate = input_event_date.text.toString()
                val eventTime = input_event_time.text.toString()
                val eventPrize = input_event_prize.text.toString()
                val eventDateFinal = "$eventDate $eventTime"
                val eventDescription = input_event_description.text.toString()
                val username = preferences.getString("username_pref","")
                sendEvent(username,eventName,eventDescription,eventPrize,eventDateFinal)
            }
        }
    }
    fun toastMessage(message: String){
        Toast.makeText(this@CreateEventActivity, message, Toast.LENGTH_LONG).show();
    }
    private fun editTextLength(editText: EditText): Int{
        return editText.text.toString().length
    }
    private fun popUpError(message: String, editText: EditText){
        editText.error = message
    }
    private fun checkTextFields(): Int{
        var checked = 0
        if(editTextLength(input_event_name) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank.",input_event_name)
        }
        if(editTextLength(input_event_date) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank.",input_event_date)
        }
        if(editTextLength(input_event_time) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank.",input_event_time)
        }
        if(editTextLength(input_event_prize) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank.",input_event_prize)
        }
        if(editTextLength(input_event_description) > 0){
            checked++
        }else{
            popUpError("This field cannot be blank.",input_event_description)
        }
        return checked
    }
    private fun sendEvent(username: String, eventName: String, eventDescription: String,eventPrize: String, eventDate: String){
        eventInterface?.createEvent(username,eventName,eventDescription,eventPrize,eventDate)?.enqueue(object : Callback<Event> {
            override fun onFailure(call: Call<Event>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
            }
            override fun onResponse(call: Call<Event>?, response: Response<Event>?) {
                if(response!!.isSuccessful){
                    toastMessage("Create Successful")
                    finish()
                    overridePendingTransition(0,0)
                }
            }
        })
    }
}