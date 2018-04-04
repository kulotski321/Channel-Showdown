package com.example.cf.channelsd.Activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.widget.Toast
import com.example.cf.channelsd.Adapters.PageViewerAdapter
import com.example.cf.channelsd.Utils.ApiUtils
import com.example.cf.channelsd.Data.User
import com.example.cf.channelsd.Fragments.*
import com.example.cf.channelsd.Interfaces.LogoutInterface
import com.example.cf.channelsd.R
import com.example.cf.channelsd.Utils.picasso
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.appbar_main.*
import kotlinx.android.synthetic.main.extension_header.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private var logoutInterface: LogoutInterface = ApiUtils.apiLogout
    private var pagerAdapter: PageViewerAdapter = PageViewerAdapter(supportFragmentManager)
    private lateinit var user: User
    private var doubleBackToExitPressedOnce = false
    private var delayHandler: Handler? = null
    private val DELAY: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar_main)

        val preferences: SharedPreferences = getSharedPreferences("MYPREFS", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        user = User(
                preferences.getString("session_key_pref", ""),
                preferences.getString("username_pref", ""),
                preferences.getString("email_pref", ""),
                preferences.getString("userType_pref", ""),
                preferences.getString("firstName_pref", ""),
                preferences.getString("lastName_pref", ""),
                preferences.getString("bio_pref", ""),
                preferences.getString("profile_pic_pref",""),
                preferences.getString("profile_vid_pref",""),
                preferences.getString("profile_thumbnail_pref","")
        )

        profile_name.text = user.username
        picasso.load(ApiUtils.BASE_URL+user.profilePicture).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(profile_picture_dashboard)
        when {
            user.userType == "0" -> {
                pagerAdapter.addFragments(LiveFragment(), "Live")
                pagerAdapter.addFragments(EpisodesFragment(), "Episodes")
                pagerAdapter.addFragments(UpcomingFragment(), "Upcoming")
                pagerAdapter.addFragments(MyEventsFragment(), "Events")
            }
            user.userType == "1" -> {
                pagerAdapter.addFragments(LiveFragment(), "Live")
                pagerAdapter.addFragments(EpisodesFragment(), "Episodes")
                pagerAdapter.addFragments(EventCommentatorFragment(), "Event")
                pagerAdapter.addFragments(HistoryCommentatorFragment(), "History")
            }
            else -> finish()
        }
        custom_viewPager.adapter = pagerAdapter
        custom_tabLayout.setupWithViewPager(custom_viewPager)

        profile_picture_dashboard.setOnClickListener {
            val i = Intent(this, ProfileActivity::class.java)
            startActivity(i)

        }
        logout_icon.setOnClickListener {
            alert("Do you really want to logout?") {
                yesButton {
                    logout(preferences.getString("session_key_pref", ""))
                    val i = Intent(this@DashboardActivity, MainActivity::class.java)
                    editor.clear()
                    editor.apply()
                    startActivity(i)
                    finish()
                }
                noButton { }
            }.show()
        }
    }

    fun toastMessage(message: String) {
        Toast.makeText(this@DashboardActivity, message, Toast.LENGTH_LONG).show();
    }

    private fun logout(session_key: String) {
        logoutInterface.logout(session_key).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.e(ContentValues.TAG, "Unable to get to API." + t?.message)
            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if (response!!.isSuccessful) {
                }
            }
        })
    }

    private val runnable: Runnable = Runnable {
        kotlin.run({
            doubleBackToExitPressedOnce = false
        })
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        toastMessage("Press BACK again to exit")
        delayHandler?.postDelayed(runnable, DELAY)
    }

    public override fun onDestroy() {
        if (delayHandler != null) {
            delayHandler!!.removeCallbacks(runnable)
        }
        super.onDestroy()
    }

}

