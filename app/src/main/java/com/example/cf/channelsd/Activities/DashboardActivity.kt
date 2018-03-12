package com.example.cf.channelsd.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.widget.Toast
import com.example.cf.channelsd.Adapters.PageViewerAdapter
import com.example.cf.channelsd.Data.ApiUtils
import com.example.cf.channelsd.Data.User
import com.example.cf.channelsd.Fragments.*
import com.example.cf.channelsd.Interfaces.LogoutInterface
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.appbar_main.*
import kotlinx.android.synthetic.main.extension_header.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import org.parceler.Parcels
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity: AppCompatActivity(){

    private var logoutInterface: LogoutInterface ?= null
    private var pagerAdapter: PageViewerAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar_main)

        val user :User = Parcels.unwrap(intent.getParcelableExtra("userinfo"))

        profile_name.text = user.username
        logoutInterface = ApiUtils.apiLogout
        pagerAdapter = PageViewerAdapter(supportFragmentManager)

        when {
            user.userType == "normal" -> {
                pagerAdapter!!.addFragments(LiveFragment(),"Live")
                pagerAdapter!!.addFragments(EpisodesFragment(),"Episodes")
                pagerAdapter!!.addFragments(UpcomingFragment(),"Upcoming")
                pagerAdapter!!.addFragments(MyEventsFragment(),"Events")
            }
            user.userType == "commentator" -> {
                pagerAdapter!!.addFragments(EventCommentatorFragment(),"Event")
                pagerAdapter!!.addFragments(HistoryCommentatorFragment(),"History")
            }
            else -> finish()
        }
        custom_viewPager.adapter = pagerAdapter
        custom_tabLayout.setupWithViewPager(custom_viewPager)

        profile_picture.setOnClickListener {
            val i = Intent(this,ProfileActivity::class.java)
            startActivity(i)
        }
        logout_icon.setOnClickListener {
            alert ("Do you really want to logout?"){
                yesButton { logout(user.session_key) }
                noButton { }
            }.show()
        }
    }
    fun toastMessage(message: String){
        Toast.makeText(this@DashboardActivity, message, Toast.LENGTH_LONG).show();
    }
    private fun logout(session_key: String){
        logoutInterface?.logout(session_key)?.enqueue(object: Callback<String>{
            override fun onFailure(call: Call<String>?, t: Throwable?) {
            }
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                if(response!!.isSuccessful){
                    val i = Intent(this@DashboardActivity,MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }

        })

    }
}
