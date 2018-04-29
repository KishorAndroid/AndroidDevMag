package com.kishordahiwadkar.androidevmag

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FeedFragment : Fragment(){

    private var mTitle: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    public fun setTitle(title: String){
        mTitle = title
    }

    public fun getTitle(): String{
        return mTitle
    }
}