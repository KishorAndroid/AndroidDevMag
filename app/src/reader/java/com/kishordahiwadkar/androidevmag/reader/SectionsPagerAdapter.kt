package com.kishordahiwadkar.androidevmag.reader

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SectionsPagerAdapter(fm: FragmentManager, items: List<FeedFragment>) : FragmentPagerAdapter(fm) {

    private var mList: List<FeedFragment>? = items

    override fun getItem(position: Int): Fragment {
        return mList!![position]
    }

    override fun getCount(): Int {
        return mList!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mList!![position].getTitle()
    }
}