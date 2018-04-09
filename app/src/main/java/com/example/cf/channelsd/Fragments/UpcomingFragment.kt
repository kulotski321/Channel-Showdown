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
import android.widget.Toast
import com.example.cf.channelsd.Adapters.EventAdapter
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Data.EventDataList
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.fragment_upcoming.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UpcomingFragment : Fragment() {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_upcoming, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timeZone : String = TimeZone.getDefault().id
        getUpcomingEventList(timeZone)
    }
    private fun getUpcomingEventList(timeZone: String){
        eventInterface.getUpcomingEventList(timeZone).enqueue(object: Callback<EventDataList>{
            override fun onFailure(call: Call<EventDataList>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API."+t?.message)
                if (t?.message == "unexpected end of stream"){
                    getUpcomingEventList(timeZone)
                }
            }

            override fun onResponse(call: Call<EventDataList>?, response: Response<EventDataList>?) {
                if(response!!.isSuccessful){
                    val events: EventDataList ?= response.body()
                    Log.e(ContentValues.TAG, response.toString())
                    if(events != null){
                        val upcomingEvents = events.events
                        val eventRecyclerviewer = upcoming_event_RV
                        eventRecyclerviewer.layoutManager = LinearLayoutManager(context,LinearLayout.VERTICAL, false)
                        val adapter = EventAdapter(upcomingEvents!!)
                        eventRecyclerviewer.adapter = adapter
                    }
                }
            }
        })
    }
    fun toastMessage(message: String) {
        Toast.makeText(view!!.context, message, Toast.LENGTH_LONG).show();
    }
}// Required empty public constructor

