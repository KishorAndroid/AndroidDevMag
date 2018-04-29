package com.kishordahiwadkar.androidevmag.reader

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kishordahiwadkar.androidevmag.R
import kotlinx.android.synthetic.reader.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
    }

    override fun onStart() {
        super.onStart()
        anonymousAuthentication()
    }

    private fun anonymousAuthentication() {
        val mAuth = FirebaseAuth.getInstance()
        var user: FirebaseUser? = mAuth.currentUser
        if (user == null) {
            authenticateUser()
        } else {
            setViewPager()
        }
    }

    private fun authenticateUser() {
        progressBar.visibility = View.VISIBLE
        container.visibility = View.GONE
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                setViewPager()
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "Error occured : " + task.exception, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setViewPager() {

        progressBar.visibility = View.GONE
        container.visibility = View.VISIBLE

        var list: MutableList<FeedFragment> = mutableListOf<FeedFragment>()

        setFragments(list)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, list)

        container.adapter = mSectionsPagerAdapter
        tabs.setupWithViewPager(container)

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    private fun setFragments(list: MutableList<FeedFragment>) {

        var articleFragment = FeedFragment()
        articleFragment.setTitle("Articles")
        list.add(articleFragment)

        var newsFragment = FeedFragment()
        newsFragment.setTitle("News")
        list.add(newsFragment)

        var videosFragment = FeedFragment()
        videosFragment.setTitle("Videos")
        list.add(videosFragment)

        var podcastFragment = FeedFragment()
        podcastFragment.setTitle("Podcasts")
        list.add(podcastFragment)
    }
}
