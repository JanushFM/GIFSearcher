package com.testapp.gifsearcher.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.testapp.gifsearcher.R
import com.testapp.gifsearcher.adapters.GifsAdapter
import com.testapp.gifsearcher.models.LoadingState
import com.testapp.gifsearcher.models.OnDisplayBigGifDialog
import com.testapp.gifsearcher.viewModels.GifsLoaderViewModel


class MainActivity : AppCompatActivity(), OnDisplayBigGifDialog {
    private lateinit var recyclerView: RecyclerView
    private lateinit var gifsLoaderVM: GifsLoaderViewModel
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var searchView: SearchView
    private lateinit var gifsAdapter: GifsAdapter
    private lateinit var loadingGifsExceptionTV: TextView
    private lateinit var retryLoadingFAB: FloatingActionButton
    private lateinit var noInternetGroup: Group
    private val searchQueryKey = "searchQuery"
    private var savedSearchQuery: CharSequence? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gifsLoaderVM = ViewModelProvider(
            this, AndroidViewModelFactory(application)
        ).get(GifsLoaderViewModel::class.java)

        recyclerView = findViewById(R.id.recycler_view_gifs)
        swipeContainer = findViewById(R.id.swipeContainer)
        loadingGifsExceptionTV = findViewById(R.id.loading_gifs_exception_TextView)
        retryLoadingFAB = findViewById(R.id.retry_loading_FAB)

        noInternetGroup = findViewById(R.id.no_internet_Group)

        setRecyclerView()
        initStateObservers()

        savedSearchQuery = savedInstanceState?.getCharSequence(searchQueryKey)

        swipeContainer.setOnRefreshListener {
            gifsLoaderVM.refreshGifs()
        }

        retryLoadingFAB.setOnClickListener {
            gifsLoaderVM.retryLoadingGifs()
        }
    }

    private fun initStateObservers() {
        gifsLoaderVM.getNetworkErrorObserverWithEmptyGifsList().observe(this, {
            noInternetGroup.visibility = View.VISIBLE
            retryLoadingFAB.visibility = View.GONE
            loadingGifsExceptionTV.text = getString(R.string.no_internet_connection)
        })

        gifsLoaderVM.getNetworkErrorObserverWithNotEmptyGifsList().observe(this) {
            retryLoadingFAB.visibility = View.VISIBLE
            Toast.makeText(
                this,
                getString(R.string.no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }

        gifsLoaderVM.getUnidentifiedErrorObserverWithNotEmptyGifsList().observe(this) {
            retryLoadingFAB.visibility = View.VISIBLE
            Toast.makeText(
                this,
                getString(R.string.unidentified_error),
                Toast.LENGTH_SHORT
            ).show()
        }

        gifsLoaderVM.getUnidentifiedErrorObserverWithEmptyGifsList().observe(this) {
            noInternetGroup.visibility = View.VISIBLE
            loadingGifsExceptionTV.text = getString(R.string.unidentified_error)
        }

        gifsLoaderVM.getLoadedStateObserverWithNotEmptyGifsList().observe(this) {
            retryLoadingFAB.visibility = View.GONE
        }

        gifsLoaderVM.getLoadedStateObserverWithEmptyGifsList().observe(this) {
            noInternetGroup.visibility = View.GONE
        }
    }

    private fun setRecyclerView() {
        val gridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.layoutManager = gridLayoutManager
        gifsAdapter = GifsAdapter(this)
        recyclerView.adapter = gifsAdapter
        gifsLoaderVM.gifsList.observe(this, {
            gifsAdapter.submitList(it)
            swipeContainer.isRefreshing = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView

        searchView.maxWidth = Int.MAX_VALUE
        if (!savedSearchQuery.isNullOrEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(savedSearchQuery, false)
            searchView.clearFocus()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                gifsLoaderVM.setQueryGifsGetter(query)
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                gifsLoaderVM.tryToSetTrendingGifsGetter()
                return true
            }

        })
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(searchQueryKey, searchView.query)
    }

    override fun displayBigGifDialog(gifURI: Uri, gifTitle: String, aspectRatio: Float) {
        val displayBigGifDialog = DisplayBigGifDialog(gifURI, gifTitle, aspectRatio)
        displayBigGifDialog.retainInstance = true

        displayBigGifDialog.show(supportFragmentManager, "displayBigGifDialog")
    }
}