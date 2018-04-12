package com.example.cf.channelsd.Adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.cf.channelsd.Activities.HistoryResultActivity
import com.example.cf.channelsd.Activities.ViewProfileActivity
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import org.parceler.Parcels
import java.util.ArrayList

class HistoryAdapter (private val eventList: ArrayList<Event>) : RecyclerView.Adapter<HistoryAdapter.ViewHolderHistory>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_history_layout, parent, false)

        return ViewHolderHistory(v)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {
        holder.eventTitleHA.text = eventList[position].eventName
        holder.eventIdHA.text = eventList[position].eventId.toString()
        holder.eventDateHA.text = eventList[position].eventDate
        holder.eventUrlHA.text = eventList[position].eventImage
        holder.eventDescriptionHA.text = eventList[position].eventDescription
        holder.eventPrizeHA.text = eventList[position].prize
        holder.contestant1HA.text = eventList[position].eventContestant1
        holder.contestant2HA.text = eventList[position].eventContestant2
        holder.votes1HA.text = eventList[position].votesContestant1.toString()
        holder.votes2HA.text = eventList[position].votesContestant2.toString()
        val numViews = eventList[position].eventViews.toString()
        holder.eventWatchViewsHA.text = " $numViews"
        holder.contextHA.picasso.load(ApiUtils.BASE_URL + holder.eventUrlHA.text).resize(50,50).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.eventImageHA)
        holder.eventImageHA.setOnClickListener{
            val i = Intent(holder.contextHA, HistoryResultActivity::class.java)
            val sendEvent = Event(
                    "username",
                    holder.eventTitleHA.text.toString(),
                    holder.eventDescriptionHA.text.toString(),
                    holder.eventPrizeHA.text.toString(),
                    holder.eventDateHA.text.toString(),
                    holder.contestant1HA.text.toString(),
                    holder.contestant2HA.text.toString(),
                    "Me",
                    holder.eventIdHA.text.toString().toInt(),
                    holder.eventUrlHA.text.toString(),
                    holder.votes1HA.text.toString().toInt(),
                    holder.votes2HA.text.toString().toInt(),
                    numViews.toInt()
            )
            i.putExtra("eventDetails", Parcels.wrap(sendEvent))
            holder.contextHA.startActivity(i)
        }
    }

    class ViewHolderHistory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemHA: CardView = itemView.findViewById(R.id.history_event_item)
        val contextHA = itemView.context!!
        val eventTitleHA : TextView = itemView.findViewById(R.id.history_event_title)
        val eventIdHA : TextView = itemView.findViewById(R.id.history_event_id)
        val eventUrlHA : TextView = itemView.findViewById(R.id.history_event_url)
        val eventImageHA : ImageView = itemView.findViewById<ImageView>(R.id.history_event_image)
        val eventPrizeHA : TextView = itemView.findViewById(R.id.history_event_prize)
        val contestant1HA : TextView = itemView.findViewById(R.id.history_event_contestant1)
        val contestant2HA : TextView = itemView.findViewById(R.id.history_event_contestant2)
        val votes1HA : TextView = itemView.findViewById(R.id.history_event_votes1)
        val votes2HA : TextView = itemView.findViewById(R.id.history_event_votes2)
        val eventDescriptionHA : TextView = itemView.findViewById(R.id.history_event_description)
        val eventDateHA : TextView = itemView.findViewById(R.id.history_event_date)
        val eventWatchViewsHA : TextView = itemView.findViewById(R.id.history_watch_views)
    }
}