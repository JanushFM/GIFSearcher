package com.testapp.gifsearcher

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.testapp.gifsearcher.adapters.GIFRecyclerViewAdapter
import com.testapp.gifsearcher.api.GiphyResponse
import com.testapp.gifsearcher.api.GiphyService
import com.testapp.gifsearcher.models.GiphyData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GIFRecyclerViewAdapter
    private val apiKey = "HiEkIy5bmsmDanYYJpIKtr65WcYXopQc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this); // Fresco needs to be initialized before you call setContentView()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view_gifs)
        setRecyclerView()

        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add( // todo should'n be called after rotation !
            GiphyService.create().getTrendingFigs(apiKey, 50,300)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> onResponse(response) }, { t -> onFailure(t) })
        )
    }

    private fun onFailure(t: Throwable) {
        Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
    }

    private fun onResponse(giphyResponse: GiphyResponse) {
        setDataRecyclerView(giphyResponse.data)
        Toast.makeText(this, "Loaded list", Toast.LENGTH_SHORT).show()
    }

    private fun setRecyclerView() {
        val gridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.layoutManager = gridLayoutManager
        adapter = GIFRecyclerViewAdapter()
        recyclerView.adapter = adapter
        adapter.stateRestorationPolicy = PREVENT_WHEN_EMPTY
    }

    private fun setDataRecyclerView(listGiphyData: List<GiphyData>) {
        adapter.listGiphyData = listGiphyData
    }
}