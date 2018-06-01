package com.kishordahiwadkar.androidevmag.reader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.AnimatedVectorDrawable
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
            holder.imageView.loadUrl(imageUrl = items[position].imageUri)
        }
    }

    class ViewHolder(private val context: Context, view: View) : RecyclerView.ViewHolder(view), Target {
        val cardView = view.cardView!!
        val rlParent = view.rlParent!!
        val title = view.textTitle!!
        val description = view.textDescription!!
        val imageView = view.imageView!!
        val imageViewLoader = view.imageViewLoader!!

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            showImageLoader()
        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {

        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            if (bitmap != null) {
                showImage(bitmap)
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

        private fun showImage(bitmap: Bitmap) {
            imageView.visibility = View.VISIBLE
            imageViewLoader.visibility = View.INVISIBLE
            val drawable = BitmapDrawable(context.resources, bitmap)
            imageView.setImageDrawable(drawable)
        }

        private fun showImageLoader() {
            imageView.visibility = View.INVISIBLE
            imageViewLoader.visibility = View.VISIBLE

            val drawable = imageViewLoader.drawable
            val avd = drawable as AnimatedVectorDrawable
            avd.start()
        }
    }
}