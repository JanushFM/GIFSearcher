package com.testapp.gifsearcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.testapp.gifsearcher.Adapters.GIFRecyclerViewAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this); // Fresco needs to be initialized before you call setContentView()
        setContentView(R.layout.activity_main)

        val listGIFs = listOf(
            Gif(
                "https://media1.giphy.com/media/mguPrVJAnEHIY/giphy.gif?cid=5088e528hsayriofz13ma3m1v2c18f2bv66w2qzg23s89naf&rid=giphy.gif",
                277,
                369
            ),
            Gif(
                "https://media4.giphy.com/media/UWV1KGPpqErVzTT4Da/giphy.gif?cid=5088e528hsayriofz13ma3m1v2c18f2bv66w2qzg23s89naf&rid=giphy.gif",
                377,
                480
            ),
            Gif(
                "https://media2.giphy.com/media/I1r5jpUvdGra8/giphy.gif?cid=5088e528hsayriofz13ma3m1v2c18f2bv66w2qzg23s89naf&rid=giphy.gif",
                290,
                449
            ),
            Gif(
                "https://media0.giphy.com/media/U5aTN7dX9aFrr2uuj8/giphy.gif?cid=5088e528hsayriofz13ma3m1v2c18f2bv66w2qzg23s89naf&rid=giphy.gif",
                270,
                480

            ),
            Gif(
                "https://media1.giphy.com/media/ercmlQ9mex00lG98FW/giphy.gif?cid=5088e528hsayriofz13ma3m1v2c18f2bv66w2qzg23s89naf&rid=giphy.gif",
                360,
                360
            ),
            Gif(
                "https://media0.giphy.com/media/iFylbW2bOTXWyHslKN/giphy-downsized.gif?cid=5088e528hsayriofz13ma3m1v2c18f2bv66w2qzg23s89naf&rid=giphy-downsized.gif",
                249,
                442
            )
        )

        recyclerView = findViewById(R.id.recycler_view_gifs)
        setRecyclerView(listGIFs)
    }

    private fun setRecyclerView(listGIFs: List<Gif>){
        val gridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.layoutManager = gridLayoutManager
        val adapter = GIFRecyclerViewAdapter()
        adapter.listGIFs = listGIFs
        recyclerView.adapter = adapter
    }
}