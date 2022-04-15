package com.example.konturtest.data

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    object Error: ResultWrapper<Nothing>()
}