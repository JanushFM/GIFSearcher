package com.testapp.gifsearcher.models

enum class LoadingState(val value: String) {
    LOADED("Loaded"), NETWORK_ERROR("Network error"), UNIDENTIFIED_ERROR("Unidentified error")
}