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

class EntriesAdapter(private val entryList: ArrayList<Entry>): RecyclerView.Adapter<EntriesAdapter.ViewHolderEntries>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEntries {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.entry_layout, parent, false)

        return ViewHolderEntries(v)
    }

    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun onBindViewHolder(holder: ViewHolderEntries, position: Int) {
        holder.username1.text = entryList[position].username

        val userStatus: String = when(entryList[position].status){
            0 -> "Pending"
            1 -> "Rejected"
            2 -> "Accepted"
            else -> "NONE"
        }
        when(userStatus){
            "Pending" -> holder.status1.setTextColor(Color.GRAY)
            "Rejected" -> holder.status1.setTextColor(Color.RED)
            "Accepted" -> holder.status1.setTextColor(Color.GREEN)
            else -> holder.status1.setTextColor(Color.BLACK)
        }
        holder.status1.text = userStatus
        holder.entryId1.text = entryList[position].entryId.toString()
        holder.item1?.setOnClickListener {
            val i = Intent(holder.context1,ViewProfileActivity::class.java)
            i.putExtra("username",holder.username1.text.toString())
            i.putExtra(("entry_id"),holder.entryId1.text.toString())
            holder.item1.context.startActivity(i)
        }
    }

    class ViewHolderEntries(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username1 = itemView.findViewById<TextView>(R.id.entry_username_user)!!
        val status1 = itemView.findViewById<TextView>(R.id.entry_status_user)!!
        val item1: LinearLayout? = itemView.findViewById<LinearLayout>(R.id.entry_item)
        val entryId1 = itemView.findViewById<TextView>(R.id.entry_event_id)!!
        val context1 = itemView.context!!
    }
}