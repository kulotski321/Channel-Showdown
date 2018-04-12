package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.Entry
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
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
        val firstName = preferences.getString("firstName_pref", "")
        val lastName = preferences.getString("lastName_pref", "")
        val bio = preferences.getString("bio_pref", "")
        val profVid = preferences.getString("profile_vid_pref", "")
        val event: Event = unwrap(intent.getParcelableExtra("eventDetails"))
        picasso.load(ApiUtils.BASE_URL + event.eventImage).into(event_image_upcoming)
        event_name_upcoming.text = event.eventName
        event_date_upcoming.text = event.eventDate
        event_description_upcoming.text = event.eventDescription
        event_prize_upcoming.text = event.prize
        event_contestant1_upcoming.text = event.eventContestant1
        event_contestant2_upcoming.text = event.eventContestant2
        event_commentator_upcoming.text = event.eventCommentator
        event_id_upcoming.text = event.eventId.toString()

        join_event_btn.setOnClickListener {
            if (firstName != "" && lastName != "" && bio != "") {
                if (profVid != "/media/profile_video/default_video.mp4") {
                    alert("Are  you sure you want to join this event?") {
                        yesButton { sendEntry(preferences.getString("username_pref", ""), event.eventId) }
                        noButton { }
                    }.show()
                } else {
                    toastMessage("Please upload a profile video to start joining events")
                }
            } else {
                toastMessage("Please complete your personal info")
            }

        }
        event_contestant1_upcoming.setOnClickListener {
            val username = event_contestant1_upcoming.text
            if (username != "Empty Slot") {
                val i = Intent(this, ViewProfileActivity::class.java)
                i.putExtra("username", username)
                i.putExtra("from_link", "true")
                i.putExtra("entry_id", "blank")
                startActivity(i)
            } else {
                toastMessage("No contestant yet")
            }
        }
        event_contestant2_upcoming.setOnClickListener {
            val username = event_contestant2_upcoming.text
            if (username != "Empty Slot") {
                val i = Intent(this, ViewProfileActivity::class.java)
                i.putExtra("username", username)
                i.putExtra("from_link", "true")
                i.putExtra("entry_id", "blank")
                startActivity(i)
            } else {
                toastMessage("No contestant yet")
            }
        }
        event_commentator_upcoming.setOnClickListener {
            val username = event_commentator_upcoming.text
            val i = Intent(this, ViewProfileActivity::class.java)
            i.putExtra("username", username)
            i.putExtra("from_link", "true")
            i.putExtra("entry_id", "blank")
            startActivity(i)
        }
    }

    private fun sendEntry(username: String, eventId: Int) {
        eventInterface.sendEntry(username, eventId).enqueue(object : Callback<Entry> {
            override fun onFailure(call: Call<Entry>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
                if (t?.message == "unexpected end of stream") {
                    sendEntry(username, eventId)
                }
            }

            override fun onResponse(call: Call<Entry>?, response: Response<Entry>?) {
                if (response!!.isSuccessful) {
                    toastMessage("Entry sent")
                } else {
                    toastMessage("You already sent an entry")
                }
            }
        })
    }

    private fun toastMessage(message: String) {
        val toast: Toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        val toastView: View = toast.view
        val toastMessage: TextView = toastView.findViewById(android.R.id.message)
        toastMessage.textSize = 20F
        toastMessage.setPadding(4, 4, 4, 4)
        toastMessage.setTextColor(Color.parseColor("#790e8b"))
        toastMessage.gravity = Gravity.CENTER
        toastView.setBackgroundColor(Color.YELLOW)
        toastView.setBackgroundResource(R.drawable.round_button1)
        toast.show()
    }
}