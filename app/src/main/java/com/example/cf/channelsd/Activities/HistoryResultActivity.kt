package com.example.cf.channelsd.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import kotlinx.android.synthetic.main.activity_history_result.*
import org.parceler.Parcels
import java.text.DateFormat
import java.util.*

class HistoryResultActivity : AppCompatActivity() {
    private var dateFormat = DateFormat.getDateTimeInstance()
    private var dateTime = Calendar.getInstance()!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_result)
        val event: Event = Parcels.unwrap(intent.getParcelableExtra("eventDetails"))
        history_result_title.text = event.eventName
        history_result_contestant1.text = event.eventContestant1
        history_result_contestant2.text = event.eventContestant2
        history_result_prize.text = event.prize
        history_result_description.text = event.eventDescription
        val year: String = event.eventDate.substring(0, 4)
        val month: String = event.eventDate.substring(5, 7)
        val day: String = event.eventDate.substring(8, 10)
        val hour: String = event.eventDate.substring(11, 13)
        val minute: String = event.eventDate.substring(14, 16)

        dateTime.set(Calendar.YEAR, year.toInt())
        dateTime.set(Calendar.MONTH, month.toInt() - 1)
        dateTime.set(Calendar.DAY_OF_MONTH, day.toInt())
        dateTime.set(Calendar.HOUR_OF_DAY, hour.toInt())
        dateTime.set(Calendar.MINUTE, minute.toInt())
        dateTime.set(Calendar.SECOND, 0)
        history_result_date.text = dateFormat.format(dateTime.time)
        picasso.load(ApiUtils.BASE_URL + event.eventImage).resize(80,80).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(history_result_image)
        show_result_btn.setOnClickListener {
            val i = Intent(this, ResultActivity::class.java)
            i.putExtra("event_id", event.eventId.toString())
            i.putExtra("eventDetails", Parcels.wrap(event))
            startActivity(i)
        }
        history_result_contestant2.setOnClickListener {
            val i = Intent(this, ViewProfileActivity::class.java)
            i.putExtra("username", event.eventContestant2)
            i.putExtra("entry_id", "blank")
            i.putExtra("from_link", "true")
            startActivity(i)
        }
        history_result_contestant1.setOnClickListener {
            val i = Intent(this, ViewProfileActivity::class.java)
            i.putExtra("username", event.eventContestant1)
            i.putExtra("entry_id", "blank")
            i.putExtra("from_link", "true")
            startActivity(i)
        }

    }
}