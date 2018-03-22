package com.example.cf.channelsd.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.cf.channelsd.Adapters.EventAdapter
import com.example.cf.channelsd.Data.ApiUtils
import com.example.cf.channelsd.Data.UpcomingEventList
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.fragment_upcoming.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingFragment : Fragment() {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_upcoming, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUpcomingEventList()
    }
    private fun getUpcomingEventList(){
        eventInterface.getUpcomingEventList().enqueue(object: Callback<UpcomingEventList>{
            override fun onFailure(call: Call<UpcomingEventList>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
                if (t?.message == "unexpected end of stream"){
                    getUpcomingEventList()
                }
            }

            override fun onResponse(call: Call<UpcomingEventList>?, response: Response<UpcomingEventList>?) {
                if(response!!.isSuccessful){
                    val events: UpcomingEventList? = response.body()
                    Log.e(ContentValues.TAG, response.toString())
                    if(events != null){
                        val upcomingEvents = events.events
                        val eventRecyclerviewer = upcoming_event_RV
                        Log.e(ContentValues.TAG, upcomingEvents.toString())
                        eventRecyclerviewer.layoutManager = LinearLayoutManager(context,LinearLayout.VERTICAL, false)
                        val adapter = EventAdapter(upcomingEvents!!)
                        eventRecyclerviewer.adapter = adapter
                    }
                }
            }
        })
    }
}// Required empty public constructor

