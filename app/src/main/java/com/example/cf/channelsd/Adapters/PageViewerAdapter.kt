package com.example.cf.channelsd.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PageViewerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager){

    var fragmentManager = fragmentManager
    var fragmentItems : ArrayList<Fragment> = ArrayList()
    var fragmentTitles: ArrayList<String> = ArrayList()

    fun addFragments(fragmentItem:Fragment,fragmentTitle:String){
        fragmentItems.add(fragmentItem)
        fragmentTitles.add(fragmentTitle)
    }
    override fun getItem(position: Int): Fragment {
        return fragmentItems[position]
    }

    override fun getCount(): Int {
        return fragmentItems.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitles[position]
    }

}