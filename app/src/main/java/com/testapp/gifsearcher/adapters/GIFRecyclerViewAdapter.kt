package com.testapp.gifsearcher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testapp.gifsearcher.GifViewHolder
import com.testapp.gifsearcher.R
import com.testapp.gifsearcher.models.GiphyData

class GIFRecyclerViewAdapter : RecyclerView.Adapter<GifViewHolder>() {
    var listGiphyData =  listOf<GiphyData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.item_gif, parent, false)
        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        holder.bind(listGiphyData[position])
    }

    override fun getItemCount(): Int = listGiphyData.size
}