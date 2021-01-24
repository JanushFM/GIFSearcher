package com.testapp.gifsearcher.models.giphyPOJOs

data class GiphyResponse(
    val data: List<GiphyData>,
    val pagination: GiphyPagination,
    val meta: GiphyMeta
)
