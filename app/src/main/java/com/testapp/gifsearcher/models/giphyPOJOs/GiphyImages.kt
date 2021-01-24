package com.testapp.gifsearcher.models.giphyPOJOs

import com.google.gson.annotations.SerializedName

data class GiphyImages(
    val original: GiphyImage,
    @SerializedName("fixed_height") val fixedHeight: GiphyImage,
    @SerializedName("fixed_height_downsampled") val fixedHeightDownsampled: GiphyImage,
    @SerializedName("fixed_height_small") val fixedHeightSmall: GiphyImage
)
