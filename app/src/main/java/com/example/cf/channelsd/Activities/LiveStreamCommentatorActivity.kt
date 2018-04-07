package com.example.cf.channelsd.Activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.cf.channelsd.R
import android.util.Log
import com.opentok.android.Session
import com.opentok.android.Stream
import com.opentok.android.Publisher
import com.opentok.android.PublisherKit
import com.opentok.android.Subscriber
import com.opentok.android.OpentokError
import android.support.annotation.NonNull
import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.view.View
import android.widget.LinearLayout
import com.example.cf.channelsd.Data.Constants.Perms.Companion.RC_VIDEO_APP_PERM
import kotlinx.android.synthetic.main.activity_livestream.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class LiveStreamCommentatorActivity : AppCompatActivity(), Session.SessionListener , PublisherKit.PublisherListener{

    var mSession: Session? = null
    val API_KEY = "46096312"
    val SESSION_ID = "2_MX40NjA5NjMxMn5-MTUyMzEwMjUxNzQzMH5LOVRENm9MZVdpejh1dGtRSUtMTmNyWGx-fg"
    val LOG_TAG = LiveStreamCommentatorActivity::class.java.simpleName
    val RC_SETTINGS_SCREEN_PERM = 123

    val TOKEN = "T1==cGFydG5lcl9pZD00NjA5NjMxMiZzaWc9MDJmYzVjNDA5NzUxOWVlMWQ1YTA5ZTc1ZGRmNGUyMWQ5OGZjNTk1MDpzZXNzaW9uX2lkPTJfTVg0ME5qQTVOak14TW41LU1UVXlNekV3TWpVeE56UXpNSDVMT1ZSRU5tOU1aVmRwZWpoMWRHdFJTVXRNVG1OeVdHeC1mZyZjcmVhdGVfdGltZT0xNTIzMTE2Nzg3Jm5vbmNlPTAuOTExNTA5ODcwNzcwNTMyNyZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTIzMjAzMTg0JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9"
    //var mPublisherViewContainer: FrameLayout? = null
    //var mSubscriberViewContainer: FrameLayout? = null
    // FOR COMMENTATOR
    var commentatorPublisherView : LinearLayout ?= null
    var commentatorPublisher: Publisher ?= null
    //var mPublisher: Publisher? = null
    // FOR CONTESTANTS
    //var mSubscriber: Subscriber? = null
    var contestant1SubscriberView : LinearLayout ?= null
    var contestant1Subscriber : Subscriber ?= null
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
            //mPublisherViewContainer =  findViewById(R.id.publisher_container);
            //mSubscriberViewContainer = findViewById(R.id.subscriber_container);
            commentatorPublisherView = findViewById(R.id.commentatorPublisher)
            contestant1SubscriberView = findViewById(R.id.contestant1Publisher)
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
        commentatorPublisher = Publisher.Builder(this)
                .name("commentator")
                .build()
        commentatorPublisherView?.addView(commentatorPublisher?.view)
        mSession?.publish(commentatorPublisher)
        commentatorLoader1.visibility = View.INVISIBLE
        commentatorLoader2.visibility = View.INVISIBLE
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
        if(contestant1Subscriber == null){
            contestant1Subscriber = Subscriber.Builder(this,stream).build()
            mSession!!.subscribe(contestant1Subscriber)
            contestant1SubscriberView?.addView(contestant1Subscriber?.view)
            contestant1Loader1.visibility = View.INVISIBLE
            contestant1Loader2.visibility = View.INVISIBLE
        }
    }

    override fun onStreamDropped(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        /*if (mSubscriber != null) {
            mSubscriber = null;
            //mSubscriberViewContainer?.removeAllViews();
        }*/
        if(stream.name == "contestant1"){
            if(contestant1Subscriber != null){
                contestant1Subscriber = null
                contestant1SubscriberView?.removeAllViews()

                contestant1Loader2.visibility = View.VISIBLE
                contestant1Loader2.text = R.string.contestant_disconnect.toString()
                Log.e("STREAM NAME:",stream.name)
            }
        }
    }

    override fun onError(session: Session, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.message)
    }
    // PublisherListener methods
    override fun onError(p0: PublisherKit?, p1: OpentokError?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStreamCreated(p0: PublisherKit?, p1: Stream?) {

    }

    override fun onStreamDestroyed(p0: PublisherKit?, p1: Stream?) {
        commentatorPublisherView?.removeAllViews()
        commentatorLoader1.visibility = View.VISIBLE
        commentatorLoader2.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mSession!!.unpublish(commentatorPublisher)
        mSession!!.disconnect()
    }

    override fun onPause() {
        super.onPause()

    }
}