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


    private fun getLoadingStateObserver(
        listCondition: () -> Boolean,
        loadingState: LoadingState,
        switchMapFunc: (GifsDataSource) -> MutableLiveData<LoadingState>
    ): LiveData<LoadingState> {

        val stateObserver = Transformations.switchMap(
            gifsDataSourceFactory.gifsDataSourceLiveData
        ) { switchMapFunc(it) }

        val mediatorStateObserver = MediatorLiveData<LoadingState>()

        mediatorStateObserver.addSource(stateObserver) {
            if (listCondition()) {
                mediatorStateObserver.value = loadingState
            }
        }

        return mediatorStateObserver
    }


    fun getNetworkErrorObserverWithNotEmptyGifsList(): LiveData<LoadingState> {
        return getLoadingStateObserver(
            getIsGifsListNotEmptyFunc(),
            LoadingState.NETWORK_ERROR,
            getNoNetworkSwitchMapFunc()
        )
    }

    fun getNetworkErrorObserverWithEmptyGifsList(): LiveData<LoadingState> {
        return getLoadingStateObserver(
            getIsGifsListEmptyFunc(),
            LoadingState.NETWORK_ERROR,
            getNoNetworkSwitchMapFunc()
        )
    }

    fun getUnidentifiedErrorObserverWithEmptyGifsList(): LiveData<LoadingState> {
        return getLoadingStateObserver(
            getIsGifsListEmptyFunc(), LoadingState.UNIDENTIFIED_ERROR,
            getUnidentifiedErrorSwitchMapFunc()
        )
    }

    fun getUnidentifiedErrorObserverWithNotEmptyGifsList(): LiveData<LoadingState> {
        return getLoadingStateObserver(
            getIsGifsListNotEmptyFunc(),
            LoadingState.UNIDENTIFIED_ERROR,
            getUnidentifiedErrorSwitchMapFunc()
        )
    }

    fun getLoadedStateObserverWithNotEmptyGifsList(): LiveData<LoadingState> {
        return getLoadingStateObserver(
            getIsGifsListNotEmptyFunc(),
            LoadingState.LOADED,
            getLoadedSwitchMapFunc()
        )
    }

    fun getLoadedStateObserverWithEmptyGifsList(): LiveData<LoadingState> {
        return getLoadingStateObserver(
            getIsGifsListEmptyFunc(),
            LoadingState.LOADED,
            getLoadedSwitchMapFunc()
        )
    }


    private fun getIsGifsListEmptyFunc(): () -> Boolean {
        return fun(): Boolean {
            return gifsList.value.isNullOrEmpty()
        }
    }

    private fun getIsGifsListNotEmptyFunc(): () -> Boolean {
        return fun(): Boolean {
            return !gifsList.value.isNullOrEmpty()
        }
    }

    private fun getNoNetworkSwitchMapFunc() =
        { gifsDataSource: GifsDataSource -> gifsDataSource.noNetworkState }

    private fun getUnidentifiedErrorSwitchMapFunc() =
        { gifsDataSource: GifsDataSource -> gifsDataSource.unidentifiedErrorState }

    private fun getLoadedSwitchMapFunc() =
        { gifsDataSource: GifsDataSource -> gifsDataSource.loadedState }

}