package com.testapp.gifsearcher.models

data class GiphyData (
    val id: String,
    val title: String,
    val username: String,
    val rating: String,
    val images: GiphyImages
)