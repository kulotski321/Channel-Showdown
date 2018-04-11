package com.example.cf.channelsd.Fragments


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Activities.CreateEventActivity
import com.example.cf.channelsd.Activities.MyEventActivity
import com.example.cf.channelsd.Data.EventData
import com.example.cf.channelsd.Data.User
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
    private lateinit var userName : String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_commentator, container, false)
    }

    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val androidId = Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
        val preferences: SharedPreferences = this.activity!!.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        val userInfo = User(
                preferences.getString("session_key_pref", ""),
                preferences.getString("username_pref", ""),
                preferences.getString("email_pref", ""),
                preferences.getString("userType_pref", ""),
                preferences.getString("firstName_pref", ""),
                preferences.getString("lastName_pref", ""),
                androidId,
                preferences.getString("bio_pref", ""),
                preferences.getString("profile_pic_pref",""),
                preferences.getString("profile_vid_pref",""),
                preferences.getString("profile_thumbnail_pref","")
        )
        userName = userInfo.username
        create_event_btn.setOnClickListener {
            if(userInfo.firstName == "" && userInfo.lastName == "" && userInfo.bio == ""){
                toastMessage("Please fill out your basic info first before creating an event")
            }else{
                val i = Intent(activity, CreateEventActivity::class.java)
                startActivity(i)
            }
        }
        my_event_btn.setOnClickListener {
            getMyEvent(userName,timeZone)
        }
    }
    private fun getMyEvent(username: String,timeZone: String) {
        eventInterface.getMyEvent(username,timeZone).enqueue(object: Callback<EventData>{
            override fun onFailure(call: Call<EventData>?, t: Throwable?) {
                if (t?.message == "unexpected end of stream"){
                    getMyEvent(username,timeZone)
                }
            }

            override fun onResponse(call: Call<EventData>?, response: Response<EventData>?) {
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
        val toast: Toast = Toast.makeText(view!!.context,message,Toast.LENGTH_LONG)
        val toastView : View = toast.view
        val toastMessage : TextView = toastView.findViewById(android.R.id.message)
        toastMessage.textSize = 16F
        toastMessage.setPadding(2,2,2,2)
        toastMessage.setTextColor(Color.parseColor("#790e8b"))
        toastMessage.gravity = Gravity.CENTER
        toastView.setBackgroundColor(Color.YELLOW)
        toastView.setBackgroundResource(R.drawable.round_button1)
        toast.show()
    }
}// Required empty public constructor
