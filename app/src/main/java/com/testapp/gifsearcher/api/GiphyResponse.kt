package com.testapp.gifsearcher.api

import com.testapp.gifsearcher.models.GiphyData
import com.testapp.gifsearcher.models.GiphyMeta
import com.testapp.gifsearcher.models.GiphyPagination

data class GiphyResponse(
    val data: List<GiphyData>,
    val pagination: GiphyPagination,
    val meta: GiphyMeta
)
