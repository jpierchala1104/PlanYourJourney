package com.example.planyourjourney.feature_planing.domain.util

sealed class APIFetchResult(val apiErrorResult: APIErrorResult? = null){
        class Success: APIFetchResult()
        class Error(apiErrorResult: APIErrorResult): APIFetchResult(apiErrorResult)
        class Loading(val isLoading: Boolean = true): APIFetchResult()
}

sealed class APIErrorResult{
        data object DataLoadError : APIErrorResult()
        data object IOExceptionError : APIErrorResult()
        data object HttpExceptionError : APIErrorResult()
}