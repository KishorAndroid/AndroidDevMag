package com.kishordahiwadkar.androidevmag.reader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kishordahiwadkar.androidevmag.R
import com.kishordahiwadkar.androidevmag.models.NewsItem
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.reader.feed_staggered_item_layout.view.*


class FeedAdapter(val items: MutableList<NewsItem>, val context: Context) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.feed_staggered_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val layoutParams = holder.cardView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = (position % 3 == 0 || (itemCount % 2 == 0 && position == itemCount - 1))

        holder.title.text = items[position].title
        holder.description.text = items[position].description
        if (!items[position].imageUri?.isEmpty()!!) {
            Picasso.with(context)
                    .load(items[position].imageUri)
                    .resize(0, context.resources.getDimensionPixelSize(R.dimen.article_image_min_height))
                    .into(holder)
        }
    }

    class ViewHolder(private val context: Context, view: View) : RecyclerView.ViewHolder(view), Target {
        val cardView = view.cardView!!
        val rlParent = view.rlParent!!
        val title = view.textTitle!!
        val description = view.textDescription!!
        val imageView = view.imageView!!

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {

        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            if (bitmap != null) {
                val drawable = BitmapDrawable(context.resources, bitmap)
                imageView.setImageDrawable(drawable)
                Palette.from(bitmap).generate { palette ->
                    val vibrantSwatch = palette.vibrantSwatch
                    rlParent.setBackgroundColor(palette.getVibrantColor(ContextCompat.getColor(context, R.color.colorAccent)))
                    if (vibrantSwatch != null) {
                        title.setTextColor(vibrantSwatch.bodyTextColor)
                        description.setTextColor(vibrantSwatch.bodyTextColor)
                    }
                }
            }
        }
    }
}