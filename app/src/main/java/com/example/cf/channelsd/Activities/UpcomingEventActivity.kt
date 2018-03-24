package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.example.cf.channelsd.Data.ApiUtils
import com.example.cf.channelsd.Data.Entry
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_upcoming_details.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import org.parceler.Parcels.unwrap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpcomingEventActivity : AppCompatActivity() {

    private var eventInterface: EventInterface = ApiUtils.apiEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_upcoming_details)

        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        val event: Event = unwrap(intent.getParcelableExtra("eventDetails"))
        event_name_upcoming.text = event.eventName
        event_date_upcoming.text = event.eventDate
        event_description_upcoming.text = event.eventDescription
        event_prize_upcoming.text = event.prize
        event_contestant1_upcoming.text = event.eventContestant1
        event_contestant2_upcoming.text = event.eventContestant2
        event_commentator_upcoming.text = event.eventCommentator
        event_id_upcoming.text = event.eventId.toString()

        join_event_btn.setOnClickListener {
            alert ("Are  you sure you want to join this event?"){
                yesButton { sendEntry(preferences.getString("username_pref", ""), event.eventId)}
                noButton { }
            }.show()
        }
        event_contestant1_upcoming.setOnClickListener {
            val username = event_contestant1_upcoming.text
            val i = Intent(this,ViewProfileActivity::class.java)
            i.putExtra("username",username)
            i.putExtra("from_link","true")
            startActivity(i)
        }
        event_contestant2_upcoming.setOnClickListener {
            val username = event_contestant2_upcoming.text
            if(username != "Empty Slot"){
                val i = Intent(this,ViewProfileActivity::class.java)
                i.putExtra("username",username)
                i.putExtra("from_link","true")
                startActivity(i)
            }else{
                toastMessage("No contestant yet")
            }
        }
        event_commentator_upcoming.setOnClickListener {
            val username = event_commentator_upcoming.text
            if(username != "Empty Slot"){
                val i = Intent(this,ViewProfileActivity::class.java)
                i.putExtra("username",username)
                i.putExtra("from_link","true")
                startActivity(i)
            }else{
                toastMessage("No contestant yet")
            }
        }
    }
    private fun sendEntry(username: String, eventId: Int){
        eventInterface.sendEntry(username,eventId).enqueue(object: Callback<Entry>{
            override fun onFailure(call: Call<Entry>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
                if(t?.message == "unexpected end of stream"){
                    sendEntry(username,eventId)
                }
            }

            override fun onResponse(call: Call<Entry>?, response: Response<Entry>?) {
                if(response!!.isSuccessful){
                    toastMessage("Entry sent")
                }else{
                    toastMessage("You already sent an entry")
                }
            }
        })
    }
    fun toastMessage(message: String) {
        Toast.makeText(this@UpcomingEventActivity, message, Toast.LENGTH_LONG).show();
    }
}