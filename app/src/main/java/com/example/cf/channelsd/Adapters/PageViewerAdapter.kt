package com.example.cf.channelsd.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PageViewerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager){

    var mfragmentManager = fragmentManager
    var mFragmentItems : ArrayList<Fragment> = ArrayList()
    var mfragmentTitles: ArrayList<String> = ArrayList()

    fun addFragments(fragmentItem:Fragment,fragmentTitle:String){
        mFragmentItems.add(fragmentItem)
        mfragmentTitles.add(fragmentTitle)
    }
    override fun getItem(position: Int): Fragment {
        return mFragmentItems[position]
    }

    override fun getCount(): Int {
        return mFragmentItems.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mfragmentTitles[position]
    }

}