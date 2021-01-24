package com.testapp.gifsearcher.models

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.testapp.gifsearcher.api.GiphyService
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyData
import io.reactivex.rxjava3.disposables.CompositeDisposable

class GifsDataSourceFactory(private val compositeDisposable: CompositeDisposable,
                            private val giphyService: GiphyService)
: DataSource.Factory<Int, GiphyData>() {
    val gifsDataSourceLiveData = MutableLiveData<GifsDataSource>()

    override fun create(): DataSource<Int, GiphyData> {
        val gifsDataSource = GifsDataSource(giphyService, compositeDisposable)
        gifsDataSourceLiveData.postValue(gifsDataSource)
        return gifsDataSource
    }

}