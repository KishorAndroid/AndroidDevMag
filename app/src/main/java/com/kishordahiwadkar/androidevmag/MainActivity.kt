package com.kishordahiwadkar.androidevmag

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        setViewPager()
    }

    private fun setViewPager() {
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
