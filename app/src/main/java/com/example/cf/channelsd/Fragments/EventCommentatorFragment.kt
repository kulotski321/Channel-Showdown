package com.example.cf.channelsd.Fragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cf.channelsd.Activities.CreateEventActivity
import com.example.cf.channelsd.Activities.MyEventActivity
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.fragment_event_commentator.*

class EventCommentatorFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_commentator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences: SharedPreferences = this.activity!!.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        create_event_btn.setOnClickListener {
            val i = Intent(activity, CreateEventActivity::class.java)
            startActivity(i)
        }
        my_event_btn.setOnClickListener {
            val i = Intent(activity, MyEventActivity::class.java)
            val username = preferences.getString("username_pref", "")
            i.putExtra("username", username)
            startActivity(i)
        }
    }
}// Required empty public constructor
