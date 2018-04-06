package com.example.cf.channelsd.Adapters

import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.cf.channelsd.Activities.ViewProfileActivity
import com.example.cf.channelsd.Data.Entry
import com.example.cf.channelsd.R
import org.w3c.dom.Text

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

        val userStatus: String = when(entryList[position].status){
            0 -> "Pending"
            1 -> "Rejected"
            2 -> "Accepted"
            else -> "NONE"
        }
        when(userStatus){
            "Pending" -> holder.status.setTextColor(Color.GRAY)
            "Rejected" -> holder.status.setTextColor(Color.RED)
            "Accepted" -> holder.status.setTextColor(Color.GREEN)
            else -> holder.status.setTextColor(Color.BLACK)
        }
        holder.status.text = userStatus
        holder.entryId.text = entryList[position].entryId.toString()
        holder.item?.setOnClickListener {
            val i = Intent(holder.context,ViewProfileActivity::class.java)
            i.putExtra("username",holder.username.text.toString())
            i.putExtra(("entry_id"),holder.entryId.text.toString())
            holder.item.context.startActivity(i)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.entry_username_user)!!
        val status = itemView.findViewById<TextView>(R.id.entry_status_user)!!
        val item: RelativeLayout? = itemView.findViewById<RelativeLayout>(R.id.entry_item)
        val entryId = itemView.findViewById<TextView>(R.id.entry_event_id)!!
        val context = itemView.context!!
    }
}