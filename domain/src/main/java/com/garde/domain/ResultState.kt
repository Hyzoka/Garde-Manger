package com.garde.domain

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
}


inline fun <T> ResultState<T>.onSuccess(action: (T) -> Unit): ResultState<T> {
    if (this is ResultState.Success) {
        action(this.data)
    }
    return this
}

inline fun <T> ResultState<T>.onError(action: (String) -> Unit): ResultState<T> {
    if (this is ResultState.Error) {
        action(this.message)
    }
    return this
}

inline fun <T> ResultState<T>.onLoading(action: () -> Unit): ResultState<T> {
    if (this is ResultState.Loading) {
        action()
    }
    return this
}
