package com.example.planyourjourney.feature_planing.domain.util

sealed class APIFetchResult(val message: String? = null){
        class Success: APIFetchResult()
        class Error(message: String): APIFetchResult(message)
        class Loading(val isLoading: Boolean = true): APIFetchResult()
}
