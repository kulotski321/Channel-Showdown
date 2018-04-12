package com.example.cf.channelsd.Activities

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.cf.channelsd.Data.Constants.Perms.Companion.LOG_TAG
import com.example.cf.channelsd.Data.Constants.Perms.Companion.RC_VIDEO_APP_PERM
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


class LiveStreamCommentatorActivity : AppCompatActivity(), Session.SessionListener, PublisherKit.PublisherListener {
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    // SESSION
    private var mSession: Session? = null
    // FOR COMMENTATOR
    private var commentatorPublisherView: LinearLayout? = null
    private var commentatorPublisher: Publisher? = null

    // FOR CONTESTANTS
    private var contestant1SubscriberView: LinearLayout? = null
    private var contestant1Subscriber: Subscriber? = null
    private var contestant2SubscriberView: LinearLayout? = null
    private var contestant2Subscriber: Subscriber? = null

    // FOR KEYS
    private var apiKey: String? = null
    private var sessionId: String? = null
    private var token: String? = null

    private var eventId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livestream)
        val btn1: Button = findViewById(R.id.vote_btn)
        val btn2: Button = findViewById(R.id.vote_contestant1_btn)
        val btn3: Button = findViewById(R.id.vote_contestant2_btn)
        val btn4: Button = findViewById(R.id.cycle_btn)
        val r1: GifImageView = findViewById(R.id.recording1)
        val r2: TextView = findViewById(R.id.recording2)
        r1.visibility = View.VISIBLE
        r2.visibility = View.VISIBLE
        btn1.visibility = View.INVISIBLE
        btn2.visibility = View.INVISIBLE
        btn3.visibility = View.INVISIBLE
        apiKey = intent.getStringExtra("api_key")
        sessionId = intent.getStringExtra("session_id")
        token = intent.getStringExtra("token")
        eventId = intent.getStringExtra("event_id")
        requestPermissions()
        btn4.setOnClickListener {
            commentatorPublisher!!.cycleCamera()
        }
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
            commentatorPublisherView = findViewById(R.id.commentatorPublisher)
            contestant1SubscriberView = findViewById(R.id.contestant1Publisher)
            contestant2SubscriberView = findViewById(R.id.contestant2Publisher)
            // initialize and connect to the session
            mSession = Session.Builder(this, apiKey, sessionId).build()
            mSession?.setSessionListener(this)
            mSession?.connect(token)

        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, *perms)
        }
    }

    private fun startArchive(eventId: Int) {
        eventInterface.startArchive(eventId).enqueue(object : Callback<ServerResponse> {
            override fun onFailure(call: Call<ServerResponse>?, t: Throwable?) {
                if (t?.message == "unexpected end of stream") {
                    startArchive(eventId)
                }
            }

            override fun onResponse(call: Call<ServerResponse>?, response: Response<ServerResponse>?) {
                if (response!!.isSuccessful) {
                } else {
                }
            }
        })
    }

    override fun onConnected(session: Session) {
        Log.i(LOG_TAG, "Session Connected")

        commentatorPublisher = Publisher.Builder(this)
                .name("commentator")
                .build()
        commentatorPublisherView?.addView(commentatorPublisher?.view)
        mSession?.publish(commentatorPublisher)
        commentatorLoader1.visibility = View.INVISIBLE
        commentatorLoader2.visibility = View.INVISIBLE
        startArchive(eventId!!.toInt())
    }

    override fun onDisconnected(session: Session) {
        Log.i(LOG_TAG, "Session Disconnected")
    }

    override fun onStreamReceived(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Received")

        Log.e("STREAM NAME RECEIVE: ", stream.name)
        if (stream.name == "contestant1") {
            if (contestant1Subscriber == null) {
                contestant1Subscriber = Subscriber.Builder(this, stream).build()
                mSession!!.subscribe(contestant1Subscriber)
                contestant1SubscriberView?.addView(contestant1Subscriber?.view)
                contestant1Loader1.visibility = View.INVISIBLE
                contestant1Loader2.visibility = View.INVISIBLE
                contestant1Loader3.visibility = View.INVISIBLE
            }
        }
        if (stream.name == "contestant2") {
            if (contestant2Subscriber == null) {
                contestant2Subscriber = Subscriber.Builder(this, stream).build()
                mSession!!.subscribe(contestant2Subscriber)
                contestant2SubscriberView?.addView(contestant2Subscriber?.view)
                contestant2Loader1.visibility = View.INVISIBLE
                contestant2Loader2.visibility = View.INVISIBLE
                contestant2Loader3.visibility = View.INVISIBLE
            }
        }
    }

    override fun onStreamDropped(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Dropped");

        Log.e("STREAM NAME RECEIVE: ", stream.name)
        if (stream.name == "contestant1") {
            if (contestant1Subscriber != null) {
                contestant1Subscriber = null
                contestant1SubscriberView?.removeAllViews()
                contestant1Loader1.visibility = View.VISIBLE
                contestant1Loader3.visibility = View.VISIBLE
                contestant1SubscriberView!!.addView(contestant1Loader1)
                contestant1SubscriberView!!.addView(contestant1Loader3)
            }
        }
        if (stream.name == "contestant2") {
            if (contestant2Subscriber != null) {
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

    // PublisherListener methods
    override fun onError(p0: PublisherKit?, p1: OpentokError?) {

    }

    override fun onStreamCreated(p0: PublisherKit?, stream: Stream) {

    }

    override fun onStreamDestroyed(p0: PublisherKit?, p1: Stream?) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        mSession!!.unpublish(commentatorPublisher)
        mSession!!.disconnect()
    }
}