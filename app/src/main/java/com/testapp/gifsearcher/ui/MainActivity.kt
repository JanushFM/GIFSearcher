package com.testapp.gifsearcher.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.testapp.gifsearcher.R
import com.testapp.gifsearcher.adapters.GifsAdapter
import com.testapp.gifsearcher.viewModels.GifsLoaderViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var gifsLoaderVM: GifsLoaderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this); // Fresco needs to be initialized before you call setContentView()
        setContentView(R.layout.activity_main)
        gifsLoaderVM = ViewModelProvider(this).get(GifsLoaderViewModel::class.java)
        recyclerView = findViewById(R.id.recycler_view_gifs)
        setRecyclerView()

        gifsLoaderVM.getState().observe(this, {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setRecyclerView() {
        val gridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.layoutManager = gridLayoutManager
        val adapter = GifsAdapter()
        recyclerView.adapter = adapter
        gifsLoaderVM.gifsList.observe(this, adapter::submitList)
    }
}