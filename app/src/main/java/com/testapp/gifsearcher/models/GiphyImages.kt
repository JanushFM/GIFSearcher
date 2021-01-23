package com.testapp.gifsearcher.models

import com.google.gson.annotations.SerializedName

data class GiphyImages(
    val original: GiphyImage,
    @SerializedName("fixed_height") val fixedHeight: GiphyImage,
    @SerializedName("fixed_height_downsampled") val fixedHeightDownsampled: GiphyImage
)
