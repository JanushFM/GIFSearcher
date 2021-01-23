package com.testapp.gifsearcher.api

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface GiphyService {
    // todo extract the same Strings to constants

    @GET("gifs/search")
    fun getGifsBySearchTerm(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Single<GiphyResponse> // I changed from call to Single

    @GET("gifs/trending")
    fun getTrendingFigs(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Single<GiphyResponse>// I changed from call to Single

    companion object {
        private const val BASE_URL = "https://api.giphy.com/v1/"

        fun create(): GiphyService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                // Returns an instance which creates asynchronous observables that run on a background thread by default.
                // Applying subscribeOn(..) has no effect on instances created by the returned factory.
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(GiphyService::class.java)
        }
    }
}