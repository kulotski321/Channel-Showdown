package com.example.cf.channelsd.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import com.example.cf.channelsd.Adapters.PageViewerAdapter
import com.example.cf.channelsd.Fragments.EpisodesFragment
import com.example.cf.channelsd.Fragments.LiveFragment
import com.example.cf.channelsd.Fragments.MyEventsFragment
import com.example.cf.channelsd.Fragments.UpcomingFragment
import com.example.cf.channelsd.R
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.appbar_main.*

/**
 * Created by CF on 3/6/2018.
 */
class DashboardActivity: AppCompatActivity(){

    var pagerAdapter: PageViewerAdapter ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar_main)

        pagerAdapter = PageViewerAdapter(supportFragmentManager)
        pagerAdapter!!.addFragments(LiveFragment(),"Live")
        pagerAdapter!!.addFragments(EpisodesFragment(),"Episodes")
        pagerAdapter!!.addFragments(UpcomingFragment(),"Upcoming")
        pagerAdapter!!.addFragments(MyEventsFragment(),"My Events")

        custom_viewPager.adapter = pagerAdapter
        custom_tabLayout.setupWithViewPager(custom_viewPager)
    }

}
