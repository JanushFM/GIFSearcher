package com.testapp.gifsearcher.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.testapp.gifsearcher.api.GiphyService
import com.testapp.gifsearcher.models.GifsDataSource
import com.testapp.gifsearcher.models.GifsDataSourceFactory
import com.testapp.gifsearcher.models.State
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable

class GifsLoaderViewModel : ViewModel() {
    private var areSearchedGifsDisplayed = false
    private var query: String = ""
    private val pageSize = 16
    private val giphyService = GiphyService.getService()
    private val compositeDisposable = CompositeDisposable()
    private val gifsDataSourceFactory: GifsDataSourceFactory
    val gifsList: LiveData<PagedList<GiphyData>>
    private val getGifsFunc = MutableLiveData<((apiKey: String, limit: Int, offset: Int) -> Single<GiphyResponse>)>()


    init {
        setTrendingGifsGetter()
        gifsDataSourceFactory = GifsDataSourceFactory(compositeDisposable, getGifsFunc)
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(pageSize * 3)
            .setPageSize(pageSize)
            .setEnablePlaceholders(false)
            .build()

        gifsList =
            Transformations.switchMap(getGifsFunc) { // switch map reacts on changes in query string
                LivePagedListBuilder(gifsDataSourceFactory, config).build()
            }
    }

    fun getState(): LiveData<State> = Transformations.switchMap(
        gifsDataSourceFactory.gifsDataSourceLiveData,
        GifsDataSource::state
    )

    fun setQueryGifsGetter(query: String) {
        getGifsFunc.value = fun(apiKey: String, limit: Int, offset: Int): Single<GiphyResponse> {
            return giphyService.getGifsBySearchTerm(apiKey, query, limit, offset)
        }
        this.query = query
        areSearchedGifsDisplayed = true
    }

    fun tryToSetTrendingGifsGetter() {
        if(areSearchedGifsDisplayed) {
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}