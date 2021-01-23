package com.testapp.gifsearcher.models

import com.google.gson.annotations.SerializedName

data class GiphyPagination(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("count") val numRequested: Int,
    val offset: Int,
)
