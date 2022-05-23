package com.example.snapshotsforreddit.util

import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first() //return one list of ResultType from database


    //check if cached list of data should be updated
    val flow = if (shouldFetch(data)) {
        //helps let us know when we should display a progress bar
        emit(Resource.Loading(data))

        try {
            //save api fetch items into database
            saveFetchResult(fetch())

            //return flow of ResultTypes(in our case, the reddit page posts)
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            //helps us display error message and display old cached data
            query().map { Resource.Error(throwable, it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}