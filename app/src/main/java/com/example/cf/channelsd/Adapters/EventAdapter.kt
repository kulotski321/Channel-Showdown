package com.example.cf.channelsd.Adapters

import android.content.ContentValues
import android.content.Intent
import android.support.v7.widget.CardView
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

class EventAdapter(private val eventList: ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.ViewHolderEvent>() {
    companion object {

    }
    private var dateFormat : DateFormat ?= null
    private var dateTime : Calendar ?= null
    private var year : String ?= null
    private var month : String ?= null
    private var day : String ?= null
    private var hour : String ?= null
    private var minute : String ?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEvent {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout, parent, false)

        return ViewHolderEvent(v)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: ViewHolderEvent, position: Int) {
        dateFormat = DateFormat.getDateTimeInstance()
        dateTime = Calendar.getInstance()!!
        year  = eventList[position].eventDate.substring(0,4)
        month  = eventList[position].eventDate.substring(5,7)
        day  = eventList[position].eventDate.substring(8,10)
        hour  = eventList[position].eventDate.substring(11,13)
        minute  = eventList[position].eventDate.substring(14,16)
        dateTime!!.set(Calendar.YEAR,year!!.toInt())
        dateTime!!.set(Calendar.MONTH,month!!.toInt()-1)
        dateTime!!.set(Calendar.DAY_OF_MONTH,day!!.toInt())
        dateTime!!.set(Calendar.HOUR_OF_DAY,hour!!.toInt())
        dateTime!!.set(Calendar.MINUTE,minute!!.toInt())
        dateTime!!.set(Calendar.SECOND,0)
        val realTime : String = dateFormat!!.format(dateTime!!.time)
        holder.eventNameEA.text = eventList[position].eventName
        holder.eventDateEA.text = realTime
        holder.eventDescriptionEA.text = eventList[position].eventDescription
        holder.eventPrizeEA.text = eventList[position].prize
        holder.eventConstant1EA.text = eventList[position].eventContestant1
        holder.eventConstant2EA.text = eventList[position].eventContestant2
        holder.eventCommentatorEA.text = eventList[position].eventCommentator
        holder.eventIdEA.text = eventList[position].eventId.toString()
        holder.eventURLEA.text = eventList[position].eventImage
        holder.contextEA.picasso.load(ApiUtils.BASE_URL + holder.eventURLEA.text).into(holder.eventImageEA)
        var contestant1 = holder.eventConstant1EA.text.toString()
        var contestant2 = holder.eventConstant2EA.text.toString()
        if (contestant1 == "") {
            contestant1 = "Empty Slot"
        }
        if (contestant2 == "") {
            contestant2 = "Empty Slot"
        }
        Log.e(ContentValues.TAG, holder.eventConstant1EA.text.toString())
        holder.itemEA.setOnClickListener {
            val i = Intent(holder.contextEA, UpcomingEventActivity::class.java)
            val event = Event(
                    "username",
                    holder.eventNameEA.text.toString(),
                    holder.eventDescriptionEA.text.toString(),
                    holder.eventPrizeEA.text.toString(),
                    holder.eventDateEA.text.toString(),
                    contestant1,
                    contestant2,
                    holder.eventCommentatorEA.text.toString(),
                    holder.eventIdEA.text.toString().toInt(),
                    holder.eventURLEA.text.toString(),
                    0,
                    0,
                    0
            )
            i.putExtra("eventDetails", Parcels.wrap(event))
            holder.itemEA.context.startActivity(i)
        }
    }

    class ViewHolderEvent(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventNameEA = itemView.findViewById<TextView>(R.id.event_name)!!
        val eventDateEA = itemView.findViewById<TextView>(R.id.event_date)!!
        val eventPrizeEA = itemView.findViewById<TextView>(R.id.event_prize)!!
        val eventDescriptionEA = itemView.findViewById<TextView>(R.id.event_description)!!
        val itemEA: CardView = itemView.findViewById(R.id.upcoming_event_item)
        val contextEA = itemView.context!!
        val eventConstant1EA = itemView.findViewById<TextView>(R.id.event_contestant1)!!
        val eventConstant2EA = itemView.findViewById<TextView>(R.id.event_contestant2)!!
        val eventCommentatorEA = itemView.findViewById<TextView>(R.id.event_commentator)!!
        val eventIdEA = itemView.findViewById<TextView>(R.id.event_id)!!
        val eventImageEA = itemView.findViewById<ImageView>(R.id.event_image)!!
        val eventURLEA = itemView.findViewById<TextView>(R.id.event_url)!!
    }

}