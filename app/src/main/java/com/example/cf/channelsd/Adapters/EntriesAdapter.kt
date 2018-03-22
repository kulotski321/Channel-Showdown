package com.example.cf.channelsd.Adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.cf.channelsd.Data.Entry
import com.example.cf.channelsd.R

class EntriesAdapter(private val entryList: ArrayList<Entry>): RecyclerView.Adapter<EntriesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.entry_layout, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = entryList[position].username
        /*val status: String
        when(holder.status.text.toString().toInt()){
            0 -> status = "Pending"
            1 -> status = ""
        }*/
        val userStatus: String = when(entryList[position].status){
            0 -> "Pending"
            1 -> "Rejected"
            2 -> "Accepted"
            else -> "NONE"
        }
        holder.status.text = userStatus
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.entry_username_user)!!
        val status = itemView.findViewById<TextView>(R.id.entry_status_user)!!
    }
}