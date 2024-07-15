package com.sportzinteractive.network.helper

import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { false },
    crossinline onFetchSuccess: () -> Unit = {},
    crossinline onFetchFailed: (Throwable) -> Unit = {},
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {

        emit(Resource.Loading(data))

        try {
            saveFetchResult(fetch())
            onFetchSuccess()
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            onFetchFailed(throwable)
            query().map { Resource.Error(NetworkThrowable(null, throwable.message ?: ""), it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}