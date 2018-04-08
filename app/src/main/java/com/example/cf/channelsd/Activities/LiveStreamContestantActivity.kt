package com.example.cf.channelsd.Activities

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.example.cf.channelsd.Data.Constants.Perms.Companion.LOG_TAG
import com.example.cf.channelsd.Data.Constants.Perms.Companion.RC_VIDEO_APP_PERM
import com.example.cf.channelsd.R
import com.opentok.android.*
import kotlinx.android.synthetic.main.activity_livestream.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions




class LiveStreamContestantActivity : AppCompatActivity(), Session.SessionListener , PublisherKit.PublisherListener{

    private var mSession: Session? = null

    // FOR CONTESTANT PUBLISHER
    private var contestant1PublisherView : LinearLayout ?= null
    private var contestant1Publisher: Publisher ?= null
    private var contestant2PublisherView : LinearLayout ?= null
    private var contestant2Publisher: Publisher ?= null
    // FOR CONTESTANT SUBSCRIBER
    private var contestant1SubscriberView : LinearLayout ?= null
    private var contestant1Subscriber : Subscriber ?= null
    private var contestant2SubscriberView : LinearLayout ?= null
    private var contestant2Subscriber : Subscriber ?= null
    // FOR COMMENTATOR SUBSCRIBER
    private var commentatorSubscriberView : LinearLayout ?= null
    private var commentatorSubscriber : Subscriber ?= null

    // FOR KEYS
    private var apiKey : String ?= null
    private var sessionId : String ?= null
    private var token : String ?= null
    private var contestant: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livestream)

        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = preferences.edit()
        contestant = intent.getStringExtra("contestant")
        apiKey = intent.getStringExtra("api_key")
        sessionId = intent.getStringExtra("session_id")
        token = intent.getStringExtra("token")
        requestPermissions()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private fun requestPermissions() {
        val perms = arrayOf(Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // initialize view objects from your layout
            if(contestant == "contestant1"){
                contestant1PublisherView = findViewById(R.id.contestant1Publisher)
                contestant2SubscriberView = findViewById(R.id.contestant2Publisher)
            }
            if(contestant == "contestant2"){
                contestant2PublisherView = findViewById(R.id.contestant2Publisher)
                contestant1SubscriberView = findViewById(R.id.contestant1Publisher)
            }

            commentatorSubscriberView = findViewById(R.id.commentatorPublisher)
            // initialize and connect to the session
            mSession = Session.Builder(this, apiKey, sessionId).build()
            mSession?.setSessionListener(this)
            mSession?.connect(token)

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, *perms)
        }
    }
    override fun onConnected(session: Session) {
        Log.i(LOG_TAG, "Session Connected")


        if(contestant == "contestant1"){
            contestant1Publisher = Publisher.Builder(this)
                    .name("contestant1")
                    .build()
            contestant1Publisher?.setPublisherListener(this)
            contestant1Loader1.visibility = View.INVISIBLE
            contestant1Loader2.visibility = View.INVISIBLE
            contestant1PublisherView?.addView(contestant1Publisher?.view)
            mSession?.publish(contestant1Publisher)
        }
        if(contestant == "contestant2"){
            contestant2Publisher = Publisher.Builder(this)
                    .name("contestant2")
                    .build()
            contestant2Publisher?.setPublisherListener(this)
            contestant2Loader1.visibility = View.INVISIBLE
            contestant2Loader2.visibility = View.INVISIBLE
            contestant2PublisherView?.addView(contestant2Publisher?.view)
            mSession?.publish(contestant2Publisher)
        }
    }

    override fun onDisconnected(session: Session) {
        Log.i(LOG_TAG, "Session Disconnected")
    }

    override fun onStreamReceived(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Received")

        Log.e("STREAM NAME RECEIVE: ",stream.name)
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
        if(stream.name == "contestant1"){
            if(contestant1Subscriber == null){
                contestant1Subscriber = Subscriber.Builder(this,stream).build()
                mSession!!.subscribe(contestant1Subscriber)
                contestant1Loader1.visibility = View.INVISIBLE
                contestant1Loader2.visibility = View.INVISIBLE
                contestant1Loader3.visibility = View.INVISIBLE
                contestant1SubscriberView?.addView(contestant1Subscriber?.view)
            }
        }
        if(stream.name == "contestant2"){
            if(contestant2Subscriber == null){
                contestant2Subscriber = Subscriber.Builder(this,stream).build()
                mSession!!.subscribe(contestant2Subscriber)
                contestant2Loader1.visibility = View.INVISIBLE
                contestant2Loader2.visibility = View.INVISIBLE
                contestant2Loader3.visibility = View.INVISIBLE
                contestant2SubscriberView?.addView(contestant2Subscriber?.view)
            }
        }
    }

    override fun onStreamDropped(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        Log.e("STREAM NAME DROPPED: ",stream.name)
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
    }

    override fun onError(session: Session, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.message)
    }

    override fun onError(p0: PublisherKit?, p1: OpentokError?) {

    }

    override fun onStreamCreated(p0: PublisherKit?, p1: Stream?) {

    }

    override fun onStreamDestroyed(p0: PublisherKit?, p1: Stream?) {

    }
    override fun onBackPressed() {
        super.onBackPressed()

        if (contestant == "contestant1") {
            mSession!!.unpublish(contestant1Publisher)
            mSession!!.disconnect()
        }
        if(contestant == "contestant2"){
            mSession!!.unpublish(contestant2Publisher)
            mSession!!.disconnect()
        }
    }
}