package com.testapp.gifsearcher

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.testapp.gifsearcher.models.GiphyData


class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val gifView: SimpleDraweeView = itemView.findViewById(R.id.my_image_view)

    fun bind(giphyData: GiphyData) {
        val uri: Uri =
            Uri.parse(giphyData.images.fixedHeight.url)

        val controller: DraweeController = Fresco.newDraweeControllerBuilder()
            .setUri(uri)
            .setAutoPlayAnimations(true)
            .build()

        gifView.aspectRatio = giphyData.images.fixedHeight.width.toFloat() / giphyData.images.fixedHeight.height.toFloat()
        gifView.controller = controller
    }

}