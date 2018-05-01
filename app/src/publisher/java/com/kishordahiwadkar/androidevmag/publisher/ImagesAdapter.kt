package com.kishordahiwadkar.androidevmag.publisher

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kishordahiwadkar.androidevmag.R
import kotlinx.android.synthetic.publisher.image_item_layout.view.*

class ImagesAdapter(val items: MutableList<String>, val context: Context, val imageAdapterClickListener: ImageAdapterClickListener) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageURI(items[position])
        holder.bind(holder, position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.simpleDraweeView!!

        fun bind(holder: ViewHolder, position: Int) {

            holder.imageView.setOnClickListener {
                imageAdapterClickListener.onImageClickListener(position)
            }
        }
    }
}