package com.testapp.gifsearcher.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.drawee.backends.pipeline.Fresco
import com.testapp.gifsearcher.R
import com.testapp.gifsearcher.adapters.GifsAdapter
import com.testapp.gifsearcher.viewModels.GifsLoaderViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var gifsLoaderVM: GifsLoaderViewModel
    private lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this); // Fresco needs to be initialized before you call setContentView()
        setContentView(R.layout.activity_main)

        gifsLoaderVM = ViewModelProvider(this).get(GifsLoaderViewModel::class.java)
        recyclerView = findViewById(R.id.recycler_view_gifs)
        swipeContainer = findViewById(R.id.swipeContainer)

        setRecyclerView()

        swipeContainer.setOnRefreshListener {
            gifsLoaderVM.refreshGifs()
        }

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
        gifsLoaderVM.gifsList.observe(this, {
            adapter.submitList(it)
            swipeContainer.isRefreshing = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                gifsLoaderVM.setGifsGetterByQuery(query)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                Toast.makeText(baseContext, "expanded !", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                gifsLoaderVM.tryToSetGifsGetterByTrends()
                return true
            }

        })
        return true
    }
}