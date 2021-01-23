package com.testapp.gifsearcher

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView


class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val gifView: SimpleDraweeView = itemView.findViewById(R.id.my_image_view)

    fun bind(gif: Gif) {
        val uri: Uri =
            Uri.parse(gif.imageURI)

        val controller: DraweeController = Fresco.newDraweeControllerBuilder()
            .setUri(uri)
            .setAutoPlayAnimations(true)
            .build()

        gifView.aspectRatio = gif.width.toFloat() / gif.height
        gifView.controller = controller
    }

}