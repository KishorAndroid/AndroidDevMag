package com.kishordahiwadkar.androidevmag.reader

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.kishordahiwadkar.androidevmag.R
import com.kishordahiwadkar.androidevmag.models.NewsItem
import kotlinx.android.synthetic.reader.fragment_main.*

class FeedFragment : Fragment() {

    private var mTabTitle: String = ""
    private var feedItemList = mutableListOf<NewsItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFeedView.adapter = FeedAdapter(feedItemList, activity!!.applicationContext)
        rvFeedView.layoutManager = LinearLayoutManager(activity)
        getFeedData()
    }

    private fun getFeedData() {

        feedItemList.clear()
        rvFeedView.adapter.notifyDataSetChanged()

        var feedDB = FirebaseDatabase.getInstance().reference
        if (mTabTitle.equals("Articles", true)) {
            articlesFeed(feedDB)
        }
    }

    private fun articlesFeed(feedDB: DatabaseReference) {
        progressBar.visibility = View.VISIBLE
        rvFeedView.visibility = View.GONE
        var feedsQuery = feedDB.child("articles")
        feedsQuery.addChildEventListener(object : ChildEventListener {

            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(datasnapshot: DataSnapshot?, p1: String?) {
                if (datasnapshot != null) {
                    var newsItem = datasnapshot.getValue(NewsItem::class.java)
                    if (newsItem != null) {
                        feedItemList.add(newsItem)
                    }
                }
                rvFeedView.adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
                rvFeedView.visibility = View.VISIBLE
            }

            override fun onChildRemoved(datasnapshot: DataSnapshot?) {
                if (datasnapshot != null) {
                    var newsItem = datasnapshot.getValue(NewsItem::class.java)
                    if (newsItem != null) {
                        if(feedItemList.contains(newsItem)) {
                            feedItemList.remove(newsItem)
                        }
                    }
                }
                rvFeedView.adapter.notifyDataSetChanged()
            }
        })
    }

    fun setTitle(title: String) {
        mTabTitle = title
    }

    fun getTitle(): String {
        return mTabTitle
    }
}