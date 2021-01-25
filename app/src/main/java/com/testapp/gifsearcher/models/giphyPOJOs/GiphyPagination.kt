package com.testapp.gifsearcher.models.giphyPOJOs

import com.google.gson.annotations.SerializedName

data class GiphyPagination(
    @SerializedName("total_count") val totalCount: String,
    @SerializedName("count") val numRequested: String,
    val offset: String,
)
