package com.example.cf.channelsd.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_upcoming_details.*
import org.parceler.Parcels.unwrap


class UpcomingEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_upcoming_details)

        val event: Event = unwrap(intent.getParcelableExtra("eventDetails"))
        event_name_upcoming.text = event.eventName
        event_date_upcoming.text = event.eventDate
        event_description_upcoming.text = event.eventDescription
        event_prize_upcoming.text = event.prize
        event_contestant1_upcoming.text = event.eventContestant1
        event_contestant2_upcoming.text = event.eventContestant2
        event_commentator_upcoming.text = event.eventCommentator
    }
}