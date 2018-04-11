package com.example.cf.channelsd.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.example.cf.channelsd.Data.Constants
import com.example.cf.channelsd.Data.Constants.Perms.Companion.LOG_TAG
import com.example.cf.channelsd.Data.Key
import com.example.cf.channelsd.Data.ServerResponse
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.opentok.android.*
import kotlinx.android.synthetic.main.activity_livestream.*
import pl.droidsonroids.gif.GifImageView
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveStreamAudienceActivity : AppCompatActivity(), Session.SessionListener ,SubscriberKit.SubscriberListener {


    private val eventInterface: EventInterface = ApiUtils.apiEvent
    // SESSION
    private var mSession: Session? = null

    // FOR KEYS
    private lateinit var apiKey : String
    private lateinit var sessionId : String
    private lateinit var token : String

    // FOR COMMENTATOR
    private var commentatorSubscriberView : LinearLayout?= null
    private var commentatorSubscriber : Subscriber?= null

    // FOR CONTESTANTS
    private var contestant1SubscriberView : LinearLayout ?= null
    private var contestant1Subscriber : Subscriber ?= null
    private var contestant2SubscriberView : LinearLayout ?= null
    private var contestant2Subscriber : Subscriber ?= null

    // FOR AUDIENCE
    private var audienceSubscriber : Subscriber ?= null
    private var mStream : Stream ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livestream)
        val btn1 : ToggleButton = findViewById(R.id.vote_btn)
        val btn2 : Button = findViewById(R.id.vote_contestant1_btn)
        val btn3 : Button = findViewById(R.id.vote_contestant2_btn)
        val btn4 : Button = findViewById(R.id.cycle_btn)
        val r1 : GifImageView = findViewById(R.id.recording1)
        val r2 : TextView = findViewById(R.id.recording2)
        r1.visibility = View.INVISIBLE
        r2.visibility = View.INVISIBLE
        btn2.visibility = View.INVISIBLE
        btn3.visibility = View.INVISIBLE
        btn4.visibility = View.INVISIBLE
        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = preferences.edit()
        val username : String = preferences.getString("username_pref", "")
        val contestant1 : String= intent.getStringExtra("contestant1")
        val contestant2 : String= intent.getStringExtra("contestant2")
        Log.e("contestant1:",contestant1)
        Log.e("contestant2:",contestant2)
        val eventId : String= intent.getStringExtra("event_id")
        getAudienceKey(username,eventId.toInt())
        btn1.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                btn2.visibility = View.VISIBLE
                btn3.visibility = View.VISIBLE
            }else{
                btn2.visibility = View.INVISIBLE
                btn3.visibility = View.INVISIBLE
            }
        }
        btn2.setOnClickListener {
            voteContestant(username,contestant1,eventId.toInt())
            btn1.isChecked = false
        }
        btn3.setOnClickListener {
            voteContestant(username,contestant2,eventId.toInt())
            btn1.isChecked = false
        }
    }
    @AfterPermissionGranted(Constants.Perms.RC_VIDEO_APP_PERM)
    private fun requestPermissions() {
        val perms = arrayOf(Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // initialize view objects from your layout
            commentatorSubscriberView = findViewById(R.id.commentatorPublisher)
            contestant1SubscriberView = findViewById(R.id.contestant1Publisher)
            contestant2SubscriberView = findViewById(R.id.contestant2Publisher)
            // initialize and connect to the session

            mSession = Session.Builder(this, apiKey, sessionId).build()
            mSession?.setSessionListener(this)
            mSession?.connect(token)

        }else{
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", Constants.Perms.RC_VIDEO_APP_PERM, *perms)
        }
    }
    private fun getAudienceKey(username: String, eventId: Int){
        eventInterface.getKeyAudience(username,eventId).enqueue(object: Callback<Key> {
            override fun onFailure(call: Call<Key>?, t: Throwable?) {
                if(t?.message == "unexpected end of stream"){
                    getAudienceKey(username,eventId)
                }
            }

            override fun onResponse(call: Call<Key>?, response: Response<Key>?) {
                if(response!!.isSuccessful){
                    val key : Key = response.body()!!
                    apiKey = key.apiKey
                    sessionId = key.sessionId
                    token = key.token
                    Log.e("apiKey",apiKey)
                    Log.e("sessionId",sessionId)
                    Log.e("token",token)
                    toastMessage("Live stream started")
                    requestPermissions()
                }else{
                    toastMessage("Wait for the commentator's stream")
                    finish()
                }
            }
        })
    }
    private fun voteContestant(username: String, contestant: String, eventId : Int){
        eventInterface.voteContestant(username,contestant,eventId).enqueue(object: Callback<ServerResponse>{
            override fun onFailure(call: Call<ServerResponse>?, t: Throwable?) {
                if(t?.message == "unexpected end of stream"){
                    voteContestant(username,contestant,eventId)
                }
            }

            override fun onResponse(call: Call<ServerResponse>?, response: Response<ServerResponse>?) {
                if(response!!.isSuccessful){
                    toastMessage("You voted $contestant")
                }else{
                    toastMessage("You already voted!")
                }
            }
        })
    }
    fun toastMessage(message: String){
        val toast: Toast = Toast.makeText(this,message,Toast.LENGTH_LONG)
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
    override fun onStreamDropped(p0: Session?, stream: Stream) {
        if(stream.name == "contestant1"){
            if(contestant1Subscriber != null){
                contestant1Subscriber = null
                contestant1SubscriberView?.removeAllViews()
                contestant1Loader1.visibility = View.VISIBLE
                contestant1Loader3.visibility = View.VISIBLE
                contestant1SubscriberView!!.addView(contestant1Loader1)
                contestant1SubscriberView!!.addView(contestant1Loader3)
            }
        }

        if(stream.name == "contestant2"){
            if(contestant2Subscriber != null){
                contestant2Subscriber = null
                contestant2SubscriberView?.removeAllViews()
                contestant2Loader1.visibility = View.VISIBLE
                contestant2Loader3.visibility = View.VISIBLE
                contestant2SubscriberView!!.addView(contestant2Loader1)
                contestant2SubscriberView!!.addView(contestant2Loader3)
            }
        }

        if(stream.name == "commentator"){
            if(commentatorSubscriber != null){
                commentatorSubscriber = null
                commentatorSubscriberView?.removeAllViews()
                commentatorLoader1.visibility = View.VISIBLE
                commentatorLoader3.visibility = View.VISIBLE
                commentatorSubscriberView!!.addView(commentatorLoader1)
                commentatorSubscriberView!!.addView(commentatorLoader3)
            }
        }
    }
    override fun onStreamReceived(p0: Session?, stream: Stream) {
        Log.e("STREAM NAME RECEIVE: ",stream.name)
        if(stream.name == "contestant1"){
            if(contestant1Subscriber == null){
                contestant1Subscriber = Subscriber.Builder(this,stream).build()
                mSession!!.subscribe(contestant1Subscriber)
                contestant1SubscriberView?.addView(contestant1Subscriber?.view)
                contestant1Loader1.visibility = View.INVISIBLE
                contestant1Loader2.visibility = View.INVISIBLE
                contestant1Loader3.visibility = View.INVISIBLE
            }
        }
        if(stream.name == "contestant2"){
            if(contestant2Subscriber == null){
                contestant2Subscriber = Subscriber.Builder(this,stream).build()
                mSession!!.subscribe(contestant2Subscriber)
                contestant2SubscriberView?.addView(contestant2Subscriber?.view)
                contestant2Loader1.visibility = View.INVISIBLE
                contestant2Loader2.visibility = View.INVISIBLE
                contestant2Loader3.visibility = View.INVISIBLE
            }
        }
        if(stream.name == "commentator"){
            if(commentatorSubscriber == null){
                commentatorSubscriber = Subscriber.Builder(this,stream).build()
                mSession!!.subscribe(commentatorSubscriber)
                commentatorLoader1.visibility = View.INVISIBLE
                commentatorLoader2.visibility = View.INVISIBLE
                commentatorLoader3.visibility = View.INVISIBLE
                commentatorSubscriberView?.addView(commentatorSubscriber?.view)
            }
        }
        audienceSubscriber = Subscriber(this,stream)
        mSession!!.subscribe(audienceSubscriber)
    }

    override fun onConnected(p0: Session?) {
        Log.i(LOG_TAG, "Session Connected")


    }
    override fun onConnected(p0: SubscriberKit?) {

    }

    override fun onDisconnected(p0: SubscriberKit?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(p0: SubscriberKit?, p1: OpentokError?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDisconnected(p0: Session?) {
        mSession!!.unsubscribe(audienceSubscriber)
        mSession!!.disconnect()
    }

    override fun onError(p0: Session?, p1: OpentokError?) {
        if (p1 != null) {
            Log.e("ERROR:",p1.message)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mSession!!.disconnect()
    }

}