package com.example.cf.channelsd.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.example.cf.channelsd.Data.Constants
import com.example.cf.channelsd.Data.Constants.Perms.Companion.LOG_TAG
import com.example.cf.channelsd.Data.Key
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.opentok.android.*
import kotlinx.android.synthetic.main.activity_livestream.*
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
    private var apiKey : String ?= null
    private var sessionId : String ?= null
    private var token : String ?= null

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

        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = preferences.edit()
        val username : String = preferences.getString("username_pref", "")
        val contestant1 : String= intent.getStringExtra("contestant1")
        val contestant2 : String= intent.getStringExtra("contestant2")
        val eventId : String= intent.getStringExtra("event_id")
        getAudienceKey(username,eventId.toInt())
        requestPermissions()
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
                    val key = response.body()
                    apiKey = key!!.apiKey
                    sessionId = key.sessionId
                    token = key.token
                    toastMessage("Live stream started")
                }else{
                    toastMessage("Live stream failed to start")
                    finish()
                }
            }
        })
    }
    fun toastMessage(message: String){
        Toast.makeText(this@LiveStreamAudienceActivity, message, Toast.LENGTH_LONG).show();
    }
    override fun onStreamDropped(p0: Session?, p1: Stream?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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