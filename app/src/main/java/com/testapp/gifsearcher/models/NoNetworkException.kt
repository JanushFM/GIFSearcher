package com.testapp.gifsearcher.models

import java.io.IOException

class NoNetworkException : IOException() {
    override fun getLocalizedMessage(): String {
        return "No internet connection"
    }
}
