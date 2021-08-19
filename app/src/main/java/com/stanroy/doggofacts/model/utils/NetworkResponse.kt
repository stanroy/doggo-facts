package com.stanroy.doggofacts.model.utils

sealed class NetworkResponse<out T> {

    data class Success<out T>(val data: T, val message: String) :
        NetworkResponse<T>()

    data class Error<T>(val message: String, val data: T? = null) :
        NetworkResponse<T>()

    data class NoInternetConnection<T>(val message: String) : NetworkResponse<T>()
}

enum class FactListStatus {
    SUCCESS, LOADING, ERROR, NO_INTERNET_CONNECTION
}