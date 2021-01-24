package com.testapp.gifsearcher.models

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.testapp.gifsearcher.api.GiphyService
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData
import io.reactivex.rxjava3.disposables.CompositeDisposable

class GifsDataSource(
    private val giphyService: GiphyService,
    private val compositeDisposable: CompositeDisposable
) : PositionalDataSource<GiphyData>() {
    val state: MutableLiveData<State> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<GiphyData>) {
        updateState(State.LOADING)
        val giphyResponse = giphyService.getTrendingFigs(
            apiKey,
            limit = params.requestedLoadSize,
            offset = params.requestedStartPosition
        )

        compositeDisposable.add(
            giphyResponse
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(
                            response.data,
                            params.requestedStartPosition,
                            response.pagination.totalCount
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
        val giphyResponse = giphyService.getTrendingFigs(
            apiKey,
            limit = params.loadSize,
            offset = params.startPosition
        )

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
}