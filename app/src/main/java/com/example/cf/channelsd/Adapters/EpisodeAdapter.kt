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
import com.example.cf.channelsd.Activities.EpisodeActivity
import com.example.cf.channelsd.Activities.HistoryResultActivity
import com.example.cf.channelsd.Data.Event
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import kotlinx.android.synthetic.main.activity_profile.*
import org.parceler.Parcels
import java.util.ArrayList

class EpisodeAdapter (private val eventList: ArrayList<Event>) : RecyclerView.Adapter<EpisodeAdapter.ViewHolderEpisode>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEpisode{
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_episode_layout, parent, false)

        return ViewHolderEpisode(v)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:ViewHolderEpisode, position: Int) {
        holder.eventTitleEA.text = eventList[position].eventName
        holder.eventIdEA.text = eventList[position].eventId.toString()
        holder.eventDateEA.text = eventList[position].eventDate
        holder.eventUrlEA.text = eventList[position].eventImage
        holder.eventDescriptionEA.text = eventList[position].eventDescription
        holder.eventPrizeEA.text = eventList[position].prize
        holder.contestant1EA.text = eventList[position].eventContestant1
        holder.contestant2EA.text = eventList[position].eventContestant2
        holder.eventCommentatorEA.text = eventList[position].eventCommentator
        holder.votes1EA.text = eventList[position].votesContestant1.toString()
        holder.votes2EA.text = eventList[position].votesContestant2.toString()
        val numViews = eventList[position].eventViews.toString()
        holder.eventWatchViewsEA.text = " $numViews"
        holder.contextEA.picasso.load(ApiUtils.BASE_URL + holder.eventUrlEA.text).resize(75,75).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.eventImageEA)
        holder.eventImageEA.setOnClickListener{
            val i = Intent(holder.contextEA, EpisodeActivity::class.java)
            val sendEvent = Event(
                    "username",
                    holder.eventTitleEA.text.toString(),
                    holder.eventDescriptionEA.text.toString(),
                    holder.eventPrizeEA.text.toString(),
                    holder.eventDateEA.text.toString(),
                    holder.contestant1EA.text.toString(),
                    holder.contestant2EA.text.toString(),
                    holder.eventCommentatorEA.text.toString(),
                    holder.eventIdEA.text.toString().toInt(),
                    holder.eventUrlEA.text.toString(),
                    holder.votes1EA.text.toString().toInt(),
                    holder.votes2EA.text.toString().toInt(),
                    numViews.toInt()
            )
            i.putExtra("eventDetails", Parcels.wrap(sendEvent))
            holder.contextEA.startActivity(i)
        }
    }
    class ViewHolderEpisode(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemEA: CardView = itemView.findViewById(R.id.episode_event_item)
        val contextEA = itemView.context!!
        val eventTitleEA : TextView = itemView.findViewById(R.id.episode_event_title)
        val eventIdEA : TextView = itemView.findViewById(R.id.episode_event_id)
        val eventUrlEA : TextView = itemView.findViewById(R.id.episode_event_url)
        val eventImageEA : ImageView = itemView.findViewById<ImageView>(R.id.episode_event_image)
        val eventPrizeEA : TextView = itemView.findViewById(R.id.episode_event_prize)
        val contestant1EA: TextView = itemView.findViewById(R.id.episode_event_contestant1)
        val contestant2EA : TextView = itemView.findViewById(R.id.episode_event_contestant2)
        val votes1EA : TextView = itemView.findViewById(R.id.episode_event_votes1)
        val votes2EA : TextView = itemView.findViewById(R.id.episode_event_votes2)
        val eventDescriptionEA : TextView = itemView.findViewById(R.id.episode_event_description)
        val eventDateEA : TextView = itemView.findViewById(R.id.episode_event_date)
        val eventCommentatorEA : TextView = itemView.findViewById(R.id.episode_event_commentator)
        val eventWatchViewsEA : TextView = itemView.findViewById(R.id.episode_watch_views)

    }

}