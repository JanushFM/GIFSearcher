package com.testapp.gifsearcher.viewModels

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.testapp.gifsearcher.api.GiphyService
import com.testapp.gifsearcher.models.GifsDataSource
import com.testapp.gifsearcher.models.GifsDataSourceFactory
import com.testapp.gifsearcher.models.LoadingState
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable

class GifsLoaderViewModel(application: Application) : AndroidViewModel(application) {
    private var areSearchedGifsDisplayed = false
    private var query: String = ""
    private val pageSize = 16
    private val giphyService = GiphyService.getService(application.baseContext)
    private val compositeDisposable = CompositeDisposable()
    private val gifsDataSourceFactory: GifsDataSourceFactory
    val gifsList: LiveData<PagedList<GiphyData>>
    private val getGifsFunc =
        MutableLiveData<((apiKey: String, limit: Int, offset: Int) -> Single<GiphyResponse>)>()


    init {
        setTrendingGifsGetter()
        gifsDataSourceFactory = GifsDataSourceFactory(compositeDisposable, getGifsFunc)
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(pageSize * 3)
            .setPageSize(pageSize)
            .setEnablePlaceholders(false)
            .build()

        gifsList = Transformations.switchMap(getGifsFunc) {
            LivePagedListBuilder(gifsDataSourceFactory, config).build()
        }
    }

    fun getLoadingStateAndIsGifsListEmpty(): LiveData<Pair<LoadingState, Boolean>> = Transformations.map(
        Transformations.switchMap(
            gifsDataSourceFactory.gifsDataSourceLiveData,
            GifsDataSource::loadingState
        )
    ) { Pair(it, gifsList.value.isNullOrEmpty()) }

    fun setQueryGifsGetter(query: String) {
        getGifsFunc.value = fun(apiKey: String, limit: Int, offset: Int): Single<GiphyResponse> {
            return giphyService.getGifsBySearchTerm(apiKey, query, limit, offset)
        }
        this.query = query
        areSearchedGifsDisplayed = true
    }

    fun tryToSetTrendingGifsGetter() {
        if (areSearchedGifsDisplayed) {
            setTrendingGifsGetter()
            areSearchedGifsDisplayed = false
        }
    }

    private fun setTrendingGifsGetter() {
        getGifsFunc.value = fun(apiKey: String, limit: Int, offset: Int): Single<GiphyResponse> {
            return giphyService.getTrendingFigs(apiKey, limit, offset)
        }
    }

    fun refreshGifs() {
        gifsDataSourceFactory.gifsDataSourceLiveData.value?.refreshGifs()
    }

    fun retryLoadingGifs() {
        gifsDataSourceFactory.gifsDataSourceLiveData.value?.retryLoadingGifs()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}