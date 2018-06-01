package com.kishordahiwadkar.androidevmag.reader

import android.widget.ImageView
import com.kishordahiwadkar.androidevmag.R
import com.squareup.picasso.Picasso

fun ImageView.loadUrl(imageUrl: String?) {
    Picasso.with(context)
            .load(imageUrl)
            .resize(0, context.resources.getDimensionPixelSize(R.dimen.article_image_min_height))
            .into(this)
}