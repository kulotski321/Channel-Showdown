package com.example.cf.channelsd.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.event_layout.view.*

class EventAdapter(private val eventList: ArrayList<Event>):RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_layout,parent,false)
        v.join_btn.setOnClickListener {
            Toast.makeText(parent.context, "Lol", Toast.LENGTH_LONG).show();
        }
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
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val eventName = itemView.findViewById<TextView>(R.id.event_name)!!
        val eventDate  = itemView.findViewById<TextView>(R.id.event_date)!!
        val eventPrize = itemView.findViewById<TextView>(R.id.event_prize)!!
        val eventDescription = itemView.findViewById<TextView>(R.id.event_description)!!
    }

}