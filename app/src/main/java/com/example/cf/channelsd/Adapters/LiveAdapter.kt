package com.example.cf.channelsd.Adapters

import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.cf.channelsd.Activities.LiveStreamAudienceActivity
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import java.util.ArrayList

class LiveAdapter(private val eventList: ArrayList<Event>) : RecyclerView.Adapter<LiveAdapter.ViewHolderLive>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveAdapter.ViewHolderLive {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_live_layout, parent, false)

        return LiveAdapter.ViewHolderLive(v)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: LiveAdapter.ViewHolderLive, position: Int) {
        holder.eventTitle.text = eventList[position].eventName
        holder.eventId.text = eventList[position].eventId.toString()
        holder.eventUrl.text = eventList[position].eventImage
        holder.contestant1.text = eventList[position].eventContestant1
        holder.contestant2.text = eventList[position].eventContestant2
        holder.context.picasso.load(ApiUtils.BASE_URL + holder.eventUrl.text).placeholder(R.drawable.logo).resize(95,95).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.eventImage)
        holder.item.setOnClickListener {
            val i  = Intent(holder.context,LiveStreamAudienceActivity::class.java)
            i.putExtra("event_id",holder.eventId.text.toString())
            i.putExtra("contestant1",holder.contestant1.text.toString())
            i.putExtra("contestant2",holder.contestant2.text.toString())
            holder.context.startActivity(i)
        }
    }
    class ViewHolderLive(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: CardView = itemView.findViewById(R.id.live_event_item)
        val context = itemView.context!!
        val eventTitle : TextView = itemView.findViewById(R.id.live_event_title)
        val eventId : TextView = itemView.findViewById(R.id.live_event_id)
        val eventUrl : TextView = itemView.findViewById(R.id.live_event_url)
        val eventImage = itemView.findViewById<ImageView>(R.id.live_event_image)!!
        val contestant1 : TextView = itemView.findViewById(R.id.live_contestant1)
        val contestant2 : TextView = itemView.findViewById(R.id.live_contestant2)

    }
}