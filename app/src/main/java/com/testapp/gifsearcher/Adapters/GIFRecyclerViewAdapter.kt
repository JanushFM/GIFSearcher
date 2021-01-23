package com.testapp.gifsearcher.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testapp.gifsearcher.Gif
import com.testapp.gifsearcher.GifViewHolder
import com.testapp.gifsearcher.R

class GIFRecyclerViewAdapter : RecyclerView.Adapter<GifViewHolder>() {
    var listGIFs =  listOf<Gif>()
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
        holder.bind(listGIFs[position])
    }

    override fun getItemCount(): Int = listGIFs.size
}