package com.testapp.gifsearcher.models

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable

class GifsDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val getGifsFunc: MutableLiveData<(apiKey: String, limit: Int, offset: Int) -> Single<GiphyResponse>>
) : DataSource.Factory<Int, GiphyData>() {
    val gifsDataSourceLiveData = MutableLiveData<GifsDataSource>()

    override fun create(): DataSource<Int, GiphyData> {
        val gifsDataSource = GifsDataSource(compositeDisposable, getGifsFunc.value!!)
        gifsDataSourceLiveData.postValue(gifsDataSource)
        return gifsDataSource
    }

}