package com.testapp.gifsearcher.adapters

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.testapp.gifsearcher.models.OnDisplayBigGifDialog
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData
import com.testapp.gifsearcher.ui.GifViewHolder

class GifsAdapter(private val onDisplayBigGifDialog: OnDisplayBigGifDialog) :
    PagedListAdapter<GiphyData, GifViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        return GifViewHolder.create(parent, onDisplayBigGifDialog)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<GiphyData>() {
            override fun areItemsTheSame(oldItem: GiphyData, newItem: GiphyData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GiphyData, newItem: GiphyData): Boolean {
                return oldItem.images.fixedHeight.url == newItem.images.fixedHeight.url
            }
        }
    }
}