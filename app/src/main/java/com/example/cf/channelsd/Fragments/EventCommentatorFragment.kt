package com.example.cf.channelsd.Fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cf.channelsd.Activities.CreateEventActivity
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.fragment_event_commentator.*

class EventCommentatorFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_commentator, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        create_event_btn.setOnClickListener {
            val i = Intent(activity,CreateEventActivity::class.java)
            startActivity(i)
        }
        upcoming_event_btn.setOnClickListener {

        }
    }
}// Required empty public constructor
