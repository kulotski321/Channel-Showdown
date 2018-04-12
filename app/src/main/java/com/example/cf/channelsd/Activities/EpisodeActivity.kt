package com.example.cf.channelsd.Activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.Data.Video
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import kotlinx.android.synthetic.main.activity_episode.*
import org.parceler.Parcels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.util.*

class EpisodeActivity : AppCompatActivity() {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    private var dateFormat = DateFormat.getDateTimeInstance()
    private var dateTime = Calendar.getInstance()!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode)
        val event: Event = Parcels.unwrap(intent.getParcelableExtra("eventDetails"))
        episode_result_title.text = event.eventName
        episode_result_contestant1.text = event.eventContestant1
        episode_result_contestant2.text = event.eventContestant2
        episode_result_prize.text = event.prize
        episode_result_description.text = event.eventDescription
        episode_result_commentator.text = event.eventCommentator
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
        episode_result_date.text = dateFormat.format(dateTime.time)
        picasso.load(ApiUtils.BASE_URL + event.eventImage).into(episode_result_image)
        show_result_btn.setOnClickListener {
            val i = Intent(this, ResultActivity::class.java)
            i.putExtra("event_id", event.eventId.toString())
            i.putExtra("eventDetails", Parcels.wrap(event))
            startActivity(i)
        }
        savedVideo(event.eventId)
        episode_result_commentator.setOnClickListener {
            val i = Intent(this, ViewProfileActivity::class.java)
            i.putExtra("username", event.eventCommentator)
            i.putExtra("entry_id", "blank")
            i.putExtra("from_link", "true")
            startActivity(i)
        }
        episode_result_contestant1.setOnClickListener {
            val i = Intent(this, ViewProfileActivity::class.java)
            i.putExtra("username", event.eventContestant1)
            i.putExtra("entry_id", "blank")
            i.putExtra("from_link", "true")
            startActivity(i)
        }
        episode_result_contestant2.setOnClickListener {
            val i = Intent(this, ViewProfileActivity::class.java)
            i.putExtra("username", event.eventContestant2)
            i.putExtra("entry_id", "blank")
            i.putExtra("from_link", "true")
            startActivity(i)
        }
    }

    private fun savedVideo(eventId: Int) {
        eventInterface.savedVideo(eventId).enqueue(object : Callback<Video> {
            override fun onFailure(call: Call<Video>?, t: Throwable?) {
                if (t?.message == "unexpected end of stream") {
                    savedVideo(eventId)
                }
            }

            override fun onResponse(call: Call<Video>?, response: Response<Video>?) {
                if (response!!.isSuccessful) {
                    val video = response.body()
                    episode_replay_btn.setOnClickListener {
                        val videoUri: Uri = Uri.parse(video!!.video)
                        val i = Intent(Intent.ACTION_VIEW, videoUri)
                        i.setDataAndType(videoUri, "video/*")
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        startActivity(i)
                    }
                } else {
                    toastMessage("Video unavailable")
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