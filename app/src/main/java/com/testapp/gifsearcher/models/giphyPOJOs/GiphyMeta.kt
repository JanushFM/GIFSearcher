package com.testapp.gifsearcher.models.giphyPOJOs

import com.google.gson.annotations.SerializedName

data class GiphyMeta(
    val status: String, // https://developers.giphy.com/docs/api/#response-codes
    val msg: String,
    @SerializedName("response_id") val responseId: String,
)
