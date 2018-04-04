package com.example.cf.channelsd.Fragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cf.channelsd.Activities.CreateEventActivity
import com.example.cf.channelsd.Activities.MyEventActivity
import com.example.cf.channelsd.Data.UpcomingEvent
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import kotlinx.android.synthetic.main.fragment_event_commentator.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EventCommentatorFragment : Fragment() {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    private val timeZone : String = TimeZone.getDefault().id
    lateinit var userName : String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_commentator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences: SharedPreferences = this.activity!!.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        val username = preferences.getString("username_pref", "")!!
        userName = username
        create_event_btn.setOnClickListener {
            val i = Intent(activity, CreateEventActivity::class.java)
            startActivity(i)
        }
        my_event_btn.setOnClickListener {
            getMyEvent(userName,timeZone)
        }
    }
    private fun getMyEvent(username: String,timeZone: String) {
        eventInterface.getMyEvent(username,timeZone).enqueue(object: Callback<UpcomingEvent>{
            override fun onFailure(call: Call<UpcomingEvent>?, t: Throwable?) {
                if (t?.message == "unexpected end of stream"){
                    getMyEvent(username,timeZone)
                }
            }

            override fun onResponse(call: Call<UpcomingEvent>?, response: Response<UpcomingEvent>?) {
                if(response!!.isSuccessful){
                    val i = Intent(activity, MyEventActivity::class.java)
                    i.putExtra("username", username)
                    startActivity(i)
                }else{
                    toastMessage("Create an event first!")
                }
            }

        })

    }
    fun toastMessage(message: String) {
        Toast.makeText(view!!.context, message, Toast.LENGTH_LONG).show();
    }
}// Required empty public constructor
