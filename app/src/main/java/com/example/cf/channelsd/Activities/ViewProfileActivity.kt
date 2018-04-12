package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.cf.channelsd.Data.Reply
import com.example.cf.channelsd.Data.User
import com.example.cf.channelsd.Interfaces.EventInterface
import com.example.cf.channelsd.Interfaces.ProfileInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import kotlinx.android.synthetic.main.activity_view_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewProfileActivity : AppCompatActivity() {
    private val profileInterface: ProfileInterface = ApiUtils.apiProfile
    private val eventInterface: EventInterface = ApiUtils.apiEvent
    private lateinit var userMain: User
    private lateinit var mEntryId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        val username = intent.getStringExtra("username")
        val entryId = intent.getStringExtra("entry_id")
        val fromLink = intent.getStringExtra("from_link")
        mEntryId = entryId
        viewProfileApplicant(username)
        view_profile_video_thumbnail.setOnClickListener {
            val videoUri: Uri = Uri.parse(ApiUtils.BASE_URL + userMain.profileVideo)
            val i = Intent(Intent.ACTION_VIEW, videoUri)
            i.setDataAndType(videoUri, "video/*")
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivity(i)
        }
        if (fromLink != "true") {
            accept_btn.setOnClickListener {
                acceptApplicant(username, entryId.toInt())
            }
            reject_btn.setOnClickListener {
                rejectApplicant(username, entryId.toInt())
            }
            back_btn.visibility = View.INVISIBLE
        } else {
            accept_btn.visibility = View.INVISIBLE
            reject_btn.visibility = View.INVISIBLE
            back_btn.setOnClickListener {
                finish()
            }
        }
    }

    private fun viewProfileApplicant(username: String) {
        profileInterface.viewProfileApplicant(username).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
                if (t?.message == "unexpected end of stream") {
                    viewProfileApplicant(username)
                }
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response!!.isSuccessful) {
                    val user: User = response.body()!!
                    userMain = user
                    Log.e("USER", user.toString())
                    val firstName = user.firstName
                    val lastName = user.lastName
                    val fullName = "$firstName $lastName"
                    view_profile_full_name.text = fullName
                    view_profile_email.text = user.email
                    if (user.bio != "") {
                        view_profile_bio.text = user.bio
                    } else {
                        view_profile_bio.text = R.string.no_description.toString()
                    }
                    picasso.load(ApiUtils.BASE_URL + user.profilePicture).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(view_profile_picture)
                    picasso.load(ApiUtils.BASE_URL + user.profileThumbNail).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(view_profile_video_thumbnail)
                }
            }
        })
    }

    private fun acceptApplicant(username: String, entryId: Int) {
        eventInterface.acceptApplicant(username, entryId).enqueue(object : Callback<Reply> {
            override fun onFailure(call: Call<Reply>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
                if (t?.message == "unexpected end of stream") {
                    acceptApplicant(username, entryId)
                }
            }

            override fun onResponse(call: Call<Reply>?, response: Response<Reply>?) {
                if (response!!.isSuccessful) {
                    toastMessage("User accepted")
                    finish()
                } else {
                    toastMessage("User already accepted")
                }
            }
        })
    }

    private fun rejectApplicant(username: String, entryId: Int) {
        eventInterface.rejectApplicant(username, entryId).enqueue(object : Callback<Reply> {
            override fun onFailure(call: Call<Reply>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
                if (t?.message == "unexpected end of stream") {
                    rejectApplicant(username, entryId)
                }
            }

            override fun onResponse(call: Call<Reply>?, response: Response<Reply>?) {
                if (response!!.isSuccessful) {
                    toastMessage("User rejected")
                    finish()
                } else {
                    toastMessage("User already rejected")
                }
            }
        })
    }

    private fun toastMessage(message: String) {
        val toast: Toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        val toastView: View = toast.view
        val toastMessage: TextView = toastView.findViewById(android.R.id.message)
        toastMessage.textSize = 20F
        toastMessage.setPadding(4, 4, 4, 4)
        toastMessage.setTextColor(Color.parseColor("#790e8b"))
        toastMessage.gravity = Gravity.CENTER
        toastView.setBackgroundColor(Color.YELLOW)
        toastView.setBackgroundResource(R.drawable.round_button1)
        toast.show()
    }

}