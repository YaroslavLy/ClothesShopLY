package com.example.clothesshop.data

sealed class Result<out T : Any> {


    data class Success<out T : Any>(val data: Any) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {

        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}