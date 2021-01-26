package com.testapp.gifsearcher.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.testapp.gifsearcher.R
import com.testapp.gifsearcher.models.OnDisplayBigGifDialog
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData


class GifViewHolder(itemView: View, private val onDisplayBigGifDialog: OnDisplayBigGifDialog) :
    RecyclerView.ViewHolder(itemView) {
    private val gifView: SimpleDraweeView = itemView.findViewById(R.id.small_gif_DraweeView)

    fun bind(giphyData: GiphyData) {
        val uri: Uri =
            Uri.parse(giphyData.images.fixedHeight.url)


        val controller: DraweeController = Fresco.newDraweeControllerBuilder()
            .setUri(uri)
            .setAutoPlayAnimations(true)
            .build()

        gifView.aspectRatio = giphyData.images.fixedHeight.width.toFloat() / giphyData.images.fixedHeight.height.toFloat()
        gifView.controller = controller

        gifView.setOnClickListener {
            onDisplayBigGifDialog.displayBigGifDialog(
                Uri.parse(giphyData.images.original.url),
                giphyData.title,
                giphyData.images.original.width.toFloat() / giphyData.images.original.height.toFloat()
            )
        }
    }

    companion object {
        fun create(parent: ViewGroup, onDisplayBigGifDialog: OnDisplayBigGifDialog): GifViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gif, parent, false)
            return GifViewHolder(view, onDisplayBigGifDialog)
        }
    }

}