package com.testapp.gifsearcher.models

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable

class GifsDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val getGifsFunc: (apiKey: String, limit: Int, offset: Int) -> Single<GiphyResponse>
) : PositionalDataSource<GiphyData>() {
    val state: MutableLiveData<State> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<GiphyData>) {
        updateState(State.LOADING)
        val giphyResponse =
            getGifsFunc(apiKey,
                params.requestedLoadSize,
                params.requestedStartPosition)

        compositeDisposable.add(
            giphyResponse
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(
                            response.data,
                            params.requestedStartPosition,
                            response.pagination.totalCount.toInt()
                        )
                    },
                    {
                        updateState(State.ERROR)
                    }
                )
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<GiphyData>) {
        updateState(State.LOADING)
        val giphyResponse =
            getGifsFunc(apiKey,
                params.loadSize,
                params.startPosition)

        compositeDisposable.add(
            giphyResponse.subscribe(
                { response ->
                    updateState(State.DONE)
                    callback.onResult(
                        response.data
                    )
                },
                {
                    updateState(State.ERROR)
                }
            )
        )
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    companion object {
        private const val apiKey = "HiEkIy5bmsmDanYYJpIKtr65WcYXopQc"
    }

    fun refreshGifs() {
        invalidate()
    }
}