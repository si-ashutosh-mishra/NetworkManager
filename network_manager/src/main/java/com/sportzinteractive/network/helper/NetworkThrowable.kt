package com.sportzinteractive.network.helper

class NetworkThrowable(val code: Int?, message: String) : Throwable(message)

sealed class Resource<T>(val data: T?) {

    class Loading<T>(data: T? = null) : Resource<T>(data)

    class Success<T>(data: T?) : Resource<T>(data)

    class Error<T>(val throwable: NetworkThrowable, data: T? = null) : Resource<T>(data)
}