package com.example.cf.channelsd.Fragments


import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Adapters.HistoryAdapter
import com.example.cf.channelsd.Adapters.LiveAdapter
import com.example.cf.channelsd.Data.EventDataList
import com.example.cf.channelsd.Interfaces.EventInterface

import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import kotlinx.android.synthetic.main.fragment_history_commentator.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HistoryCommentatorFragment : Fragment() {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    private var eventRecyclerviewer: RecyclerView? = null
    private var noEventImage: ImageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_commentator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences: SharedPreferences = context!!.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        val username: String = preferences.getString("username_pref", "")
        noEventImage = no_history_img
        eventRecyclerviewer = history_event_RV
        val timeZone: String = TimeZone.getDefault().id
        getHistoryList(username, timeZone)
    }

    fun toastMessage(message: String) {
        val toast: Toast = Toast.makeText(view!!.context, message, Toast.LENGTH_LONG)
        val toastView: View = toast.view
        val toastMessage: TextView = toastView.findViewById(android.R.id.message)
        toastMessage.textSize = 16F
        toastMessage.setPadding(2, 2, 2, 2)
        toastMessage.setTextColor(Color.parseColor("#790e8b"))
        toastMessage.gravity = Gravity.CENTER
        toastView.setBackgroundColor(Color.YELLOW)
        toastView.setBackgroundResource(R.drawable.round_button1)
        toast.show()
    }

    private fun getHistoryList(username: String, timeZone: String) {
        eventInterface.getHistoryList(username, timeZone).enqueue(object : Callback<EventDataList> {
            override fun onFailure(call: Call<EventDataList>?, t: Throwable?) {
                if (t?.message == "unexpected end of stream") {
                    getHistoryList(username, timeZone)
                }
            }

            override fun onResponse(call: Call<EventDataList>?, response: Response<EventDataList>?) {
                if (response!!.isSuccessful) {
                    val events: EventDataList? = response.body()
                    if (events != null) {
                        val historyEvents = events.events
                        if (historyEvents != null) {
                            if (historyEvents.isNotEmpty()) {
                                Log.e("DATA:", historyEvents.toString())
                                eventRecyclerviewer!!.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
                                val adapter = HistoryAdapter(historyEvents)
                                eventRecyclerviewer!!.adapter = adapter
                                noEventImage!!.visibility = View.GONE
                            } else {
                                noEventImage!!.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        })
    }
}