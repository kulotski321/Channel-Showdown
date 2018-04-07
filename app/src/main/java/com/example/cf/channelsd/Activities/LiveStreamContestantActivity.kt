package com.example.cf.channelsd.Activities

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.example.cf.channelsd.Data.Constants.Perms.Companion.RC_VIDEO_APP_PERM
import com.example.cf.channelsd.R
import com.opentok.android.*
import kotlinx.android.synthetic.main.activity_livestream.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions




class LiveStreamContestantActivity : AppCompatActivity(), Session.SessionListener , PublisherKit.PublisherListener{

    var mSession: Session? = null
    val API_KEY = "46096312"
    val SESSION_ID = "2_MX40NjA5NjMxMn5-MTUyMzEwMjUxNzQzMH5LOVRENm9MZVdpejh1dGtRSUtMTmNyWGx-fg"
    val TOKEN = "T1==cGFydG5lcl9pZD00NjA5NjMxMiZzaWc9ZmU2NjRhNjFmM2NjMDE5MTBhMjA4MzIyMTNkOTU5MDAzMjU1ZGMyYjpzZXNzaW9uX2lkPTJfTVg0ME5qQTVOak14TW41LU1UVXlNekV3TWpVeE56UXpNSDVMT1ZSRU5tOU1aVmRwZWpoMWRHdFJTVXRNVG1OeVdHeC1mZyZjcmVhdGVfdGltZT0xNTIzMTE5NTQzJm5vbmNlPTAuODg5NDk3NjgxNjkxNjgwMiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTIzMjA1OTQwJmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9"
    val LOG_TAG = LiveStreamCommentatorActivity::class.java.simpleName
    val RC_SETTINGS_SCREEN_PERM = 123
    //var mPublisherViewContainer: FrameLayout? = null
    //var mSubscriberViewContainer: FrameLayout? = null

    // FOR CONTESTANTS
    var contestantant1PublisherView : LinearLayout?= null
    var contestantant1Publisher: Publisher?= null
    var contestantant2PublisherView : LinearLayout?= null
    var contestantant2Publisher: Publisher?= null

    //var mPublisher: Publisher? = null
    //var mSubscriber: Subscriber? = null
    // FOR COMMENTATOR
    var commentatorSubscriberView : LinearLayout ?= null
    var commentatorSubscriber : Subscriber ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livestream)

        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        //val editor: SharedPreferences.Editor = preferences.edit()
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
            contestantant1PublisherView = findViewById(R.id.contestant1Publisher)
            commentatorSubscriberView = findViewById(R.id.commentatorPublisher)
            // initialize and connect to the session
            mSession = Session.Builder(this, API_KEY, SESSION_ID).build()
            mSession?.setSessionListener(this)
            mSession?.connect(TOKEN)

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, *perms)
        }
    }
    override fun onConnected(session: Session) {
        Log.i(LOG_TAG, "Session Connected")
        //mPublisher = Publisher.Builder(this).build()
        //mPublisher?.setPublisherListener(this)
        //mPublisherViewContainer?.addView(mPublisher?.view)
        //mSession?.publish(mPublisher)
        contestantant1Publisher = Publisher.Builder(this)
                .name("contestant1")
                .build()
        contestantant1Publisher?.setPublisherListener(this)
        contestant1Loader1.visibility = View.INVISIBLE
        contestant1Loader2.visibility = View.INVISIBLE
        contestantant1PublisherView?.addView(contestantant1Publisher?.view)
        mSession?.publish(contestantant1Publisher)
    }

    override fun onDisconnected(session: Session) {
        Log.i(LOG_TAG, "Session Disconnected")
    }

    override fun onStreamReceived(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Received")

        /*if (mSubscriber == null) {
            mSubscriber = Subscriber.Builder(this, stream).build()
            mSession?.subscribe(mSubscriber)
            //mSubscriberViewContainer?.addView(mSubscriber?.view)
        }*/
        if(commentatorSubscriber == null){
            commentatorSubscriber = Subscriber.Builder(this,stream).build()
            mSession!!.subscribe(commentatorSubscriber)
            commentatorLoader1.visibility = View.INVISIBLE
            commentatorLoader2.visibility = View.INVISIBLE
            commentatorSubscriberView?.addView(commentatorSubscriber?.view)
        }
    }

    override fun onStreamDropped(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Dropped");


        Log.e("STREAM NAME:",stream.name)
        if(commentatorSubscriber != null){
            commentatorSubscriber = null
            commentatorSubscriberView?.removeAllViews()
            commentatorLoader2.visibility = View.VISIBLE
            commentatorLoader2.text = R.string.commentator_disconnect.toString()
        }
    }

    override fun onError(session: Session, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.message)
    }

    override fun onError(p0: PublisherKit?, p1: OpentokError?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStreamCreated(p0: PublisherKit?, p1: Stream?) {

    }

    override fun onStreamDestroyed(p0: PublisherKit?, p1: Stream?) {

    }
    override fun onBackPressed() {
        super.onBackPressed()
        mSession!!.unpublish(contestantant1Publisher)
        mSession!!.disconnect()
    }
}