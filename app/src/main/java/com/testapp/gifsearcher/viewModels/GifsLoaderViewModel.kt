package com.testapp.gifsearcher.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.testapp.gifsearcher.api.GiphyService
import com.testapp.gifsearcher.models.GifsDataSource
import com.testapp.gifsearcher.models.GifsDataSourceFactory
import com.testapp.gifsearcher.models.State
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData
import io.reactivex.rxjava3.disposables.CompositeDisposable

class GifsLoaderViewModel : ViewModel() {
    private val pageSize = 16
    private val giphyService = GiphyService.getService()
    private val compositeDisposable = CompositeDisposable()
    private val gifsDataSourceFactory: GifsDataSourceFactory
    val gifsList: LiveData<PagedList<GiphyData>>

    init {
        gifsDataSourceFactory = GifsDataSourceFactory(compositeDisposable, giphyService)
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(pageSize * 3)
            .setPageSize(pageSize)
            .setEnablePlaceholders(false)
            .build()

        gifsList = LivePagedListBuilder(gifsDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap(
        gifsDataSourceFactory.gifsDataSourceLiveData,
        GifsDataSource::state
    )

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}