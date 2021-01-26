package com.testapp.gifsearcher.models

import android.net.Uri

interface OnDisplayBigGifDialog {
    fun displayBigGifDialog(gifURI: Uri,
                            gifTitle: String,
                            aspectRatio: Float)
}