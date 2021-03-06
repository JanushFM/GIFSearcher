package com.testapp.gifsearcher.api

import android.content.Context
import com.testapp.gifsearcher.models.NetworkConnectionInterceptor
import com.testapp.gifsearcher.models.giphyPOJOs.GiphyResponse
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface GiphyService {
    @GET("gifs/search")
    fun getGifsBySearchTerm(
        @Query(API_KEY_QUERY) apiKey: String,
        @Query("q") query: String,
        @Query(LIMIT_QUERY) limit: Int = 10,
        @Query(OFFSET_QUERY) offset: Int = 0
    ): Single<GiphyResponse>

    @GET("gifs/trending")
    fun getTrendingFigs(
        @Query(API_KEY_QUERY) apiKey: String,
        @Query(LIMIT_QUERY) limit: Int = 10,
        @Query(OFFSET_QUERY) offset: Int = 0
    ): Single<GiphyResponse>

    companion object {
        private const val BASE_URL = "https://api.giphy.com/v1/"
        private const val API_KEY_QUERY = "api_key"
        private const val LIMIT_QUERY = "limit"
        private const val OFFSET_QUERY = "offset"

        fun getService(context: Context): GiphyService {
            val oktHttpClient = OkHttpClient.Builder()
                .addInterceptor(NetworkConnectionInterceptor(context))

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
                .baseUrl(BASE_URL)
                .client(oktHttpClient.build())
                .build()

            return retrofit.create(GiphyService::class.java)
        }
    }
}