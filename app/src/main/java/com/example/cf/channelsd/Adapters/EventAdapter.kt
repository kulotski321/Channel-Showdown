package com.example.cf.channelsd.Adapters

import android.content.ContentValues
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.cf.channelsd.Activities.UpcomingEventActivity
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.R
import org.parceler.Parcels

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
        holder.eventName.text = eventList[position].eventName
        holder.eventDate.text = eventList[position].eventDate
        holder.eventDescription.text = eventList[position].eventDescription
        holder.eventPrize.text = eventList[position].prize
        holder.eventConstant1.text = eventList[position].eventContestant1
        holder.eventConstant2.text = eventList[position].eventContestant2
        holder.eventCommentor.text = eventList[position].eventCommentator
        var contestant1 = holder.eventConstant1.text.toString()
        var contestant2 = holder.eventConstant2.text.toString()
        if (contestant1 == "") {
            contestant1 = "Empty slot"
        }
        if (contestant2 == "") {
            contestant2 = "Empty slot"
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
                    holder.eventCommentor.text.toString()
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
    }

}