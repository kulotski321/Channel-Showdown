package com.example.cf.channelsd.Adapters

import android.content.ContentValues
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.cf.channelsd.Activities.UpcomingEventActivity
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import org.parceler.Parcels
import com.example.cf.channelsd.Utils.picasso
import java.text.DateFormat
import java.util.*

class EventAdapter(private val eventList: ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    companion object {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var dateFormat = DateFormat.getDateTimeInstance()
        var dateTime = Calendar.getInstance()!!
        val year : String = eventList[position].eventDate.substring(0,4)
        val month : String = eventList[position].eventDate.substring(5,7)
        val day : String = eventList[position].eventDate.substring(8,10)
        val hour : String = eventList[position].eventDate.substring(11,13)
        val minute : String = eventList[position].eventDate.substring(14,16)
        dateTime.set(Calendar.YEAR,year.toInt())
        dateTime.set(Calendar.MONTH,month.toInt()-1)
        dateTime.set(Calendar.DAY_OF_MONTH,day.toInt())
        dateTime.set(Calendar.HOUR_OF_DAY,hour.toInt())
        dateTime.set(Calendar.MINUTE,minute.toInt())
        dateTime.set(Calendar.SECOND,0)
        val realTime : String = dateFormat.format(dateTime.time)
        holder.eventName.text = eventList[position].eventName
        holder.eventDate.text = realTime// eventList[position].eventDate
        holder.eventDescription.text = eventList[position].eventDescription
        holder.eventPrize.text = eventList[position].prize
        holder.eventConstant1.text = eventList[position].eventContestant1
        holder.eventConstant2.text = eventList[position].eventContestant2
        holder.eventCommentor.text = eventList[position].eventCommentator
        holder.eventId.text = eventList[position].eventId.toString()
        holder.eventURL.text = eventList[position].eventImage
        holder.context.picasso.load(ApiUtils.BASE_URL + holder.eventURL.text).into(holder.eventImage)
        var contestant1 = holder.eventConstant1.text.toString()
        var contestant2 = holder.eventConstant2.text.toString()
        if (contestant1 == "") {
            contestant1 = "Empty Slot"
        }
        if (contestant2 == "") {
            contestant2 = "Empty Slot"
        }
        Log.e(ContentValues.TAG, holder.eventConstant1.text.toString())
        holder.item?.setOnClickListener {
            val i = Intent(holder.context, UpcomingEventActivity::class.java)
            val event = Event(
                    "username",
                    holder.eventName.text.toString(),
                    holder.eventDescription.text.toString(),
                    holder.eventPrize.text.toString(),
                    holder.eventDate.text.toString(),
                    contestant1,
                    contestant2,
                    holder.eventCommentor.text.toString(),
                    holder.eventId.text.toString().toInt(),
                    holder.eventURL.text.toString()

            )
            i.putExtra("eventDetails", Parcels.wrap(event))
            holder.item.context.startActivity(i)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName = itemView.findViewById<TextView>(R.id.event_name)!!
        val eventDate = itemView.findViewById<TextView>(R.id.event_date)!!
        val eventPrize = itemView.findViewById<TextView>(R.id.event_prize)!!
        val eventDescription = itemView.findViewById<TextView>(R.id.event_description)!!
        val item: LinearLayout? = itemView.findViewById<LinearLayout>(R.id.upcoming_event_item)
        val context = itemView.context!!
        val eventConstant1 = itemView.findViewById<TextView>(R.id.event_contestant1)!!
        val eventConstant2 = itemView.findViewById<TextView>(R.id.event_contestant2)!!
        val eventCommentor = itemView.findViewById<TextView>(R.id.event_commentator)!!
        val eventId = itemView.findViewById<TextView>(R.id.event_id)!!
        val eventImage = itemView.findViewById<ImageView>(R.id.event_image)!!
        val eventURL = itemView.findViewById<TextView>(R.id.event_url)!!
    }

}