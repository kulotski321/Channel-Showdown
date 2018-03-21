package com.example.cf.channelsd.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.fragment_live.*

class LiveFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_live, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        live_btn.setOnClickListener {
            toastMessage("TEST")
        }
    }

    fun toastMessage(message: String) {
        Toast.makeText(view!!.context, message, Toast.LENGTH_LONG).show();
    }
}// Required empty public constructor
