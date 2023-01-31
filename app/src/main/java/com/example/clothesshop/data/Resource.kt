package com.example.clothesshop.data

// todo #8 remove clas resource
sealed class Resource<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(error: String, data: T? = null) : Resource<T>(data = data, error = error)
}