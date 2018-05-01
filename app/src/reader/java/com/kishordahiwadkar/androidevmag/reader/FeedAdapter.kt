package com.kishordahiwadkar.androidevmag.reader

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kishordahiwadkar.androidevmag.R
import com.kishordahiwadkar.androidevmag.models.NewsItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.reader.feed_item_layout.view.*

class FeedAdapter(val items: MutableList<NewsItem>, val context: Context) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.feed_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = items[position].title
        holder.description.text = items[position].description
        if (!items[position].imageUri?.isEmpty()!!) {
            Picasso.get().load(items[position].imageUri).into(holder.imageView)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.textTitle!!
        val description = view.textDescription!!
        val imageView = view.imageView!!
    }
}