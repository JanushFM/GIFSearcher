package com.testapp.gifsearcher.models

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.schedulers.Schedulers

class GifsDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val getGifsFunc: (apiKey: String, limit: Int, offset: Int) -> Single<GiphyResponse>
) : PositionalDataSource<GiphyData>() {
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    val noNetworkState: MutableLiveData<LoadingState> = MutableLiveData()
    val unidentifiedErrorState: MutableLiveData<LoadingState> = MutableLiveData()
    val loadedState: MutableLiveData<LoadingState> = MutableLiveData()

    private lateinit var retryCompletable: Completable

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<GiphyData>) {
        val giphyResponse =
            getGifsFunc(
                apiKey,
                params.requestedLoadSize,
                params.requestedStartPosition
            )

        compositeDisposable.add(
            giphyResponse
                .subscribe(
                    { response ->
                        updateLoadedState()
                        callback.onResult(
                            response.data,
                            params.requestedStartPosition,
                            response.pagination.totalCount.toInt()
                        )
                    },
                    {
                        if (it is NoNetworkException) {
                            updateNoNetworkState()
                        } else {
                            updateUnidentifiedErrorState()
                        }
                        setRetryLoadingGifsAction { loadInitial(params, callback) }
                    }
                )
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<GiphyData>) {
        val giphyResponse =
            getGifsFunc(
                apiKey,
                params.loadSize,
                params.startPosition
            )

        compositeDisposable.add(
            giphyResponse.subscribe(
                { response ->
                    updateLoadedState()
                    callback.onResult(
                        response.data
                    )
                },
                {
                    if (it is NoNetworkException) {
                        updateNoNetworkState()
                    } else {
                        updateUnidentifiedErrorState()
                    }
                    setRetryLoadingGifsAction { loadRange(params, callback) }
                }
            )
        )
    }

    private fun updateNoNetworkState() {
        noNetworkState.postValue(LoadingState.NETWORK_ERROR)
    }

    private fun updateUnidentifiedErrorState() {
        unidentifiedErrorState.postValue(LoadingState.UNIDENTIFIED_ERROR)
    }

    private fun updateLoadedState() {
        loadedState.postValue(LoadingState.LOADED)
    }

    fun refreshGifs() {
        invalidate()
    }

    fun retryLoadingGifs() {
        compositeDisposable.add(
            retryCompletable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    private fun setRetryLoadingGifsAction(action: Action?) {
        if (action != null) {
            retryCompletable = Completable.fromAction(action)
        }
    }

    companion object {
        private const val apiKey = "HiEkIy5bmsmDanYYJpIKtr65WcYXopQc"
    }
}