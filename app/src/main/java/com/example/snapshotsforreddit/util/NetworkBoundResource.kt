package com.example.snapshotsforreddit.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = channelFlow  {
    val data = query().first() //return one list of ResultType from database

    //check if cached list of data should be updated
    if (shouldFetch(data)) {
        //helps let us know when we should display a progress bar
        val loading = launch {
            query().collect { send(Resource.Loading(it)) }
        }

        try {
            //save data from api into database
            saveFetchResult(fetch())

            //when we finish fetching the data, we want to cancel the loading coroutine
            loading.cancel()

            //return flow of ResultTypes(in our case, the reddit page posts)
            query().collect { send(Resource.Success(it)) }
        } catch (throwable: Throwable) {
            //helps us display error message and display old cached data
            query().collect { send(Resource.Error(throwable, it)) }
        }
    } else {
        query().collect { send(Resource.Success(it)) }
    }


}