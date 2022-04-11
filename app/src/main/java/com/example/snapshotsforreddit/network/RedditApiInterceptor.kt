package com.example.snapshotsforreddit.network


import com.example.snapshotsforreddit.data.repository.AuthDataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RedditApiInterceptor @Inject constructor(private val authDataStoreRepository: AuthDataStoreRepository) :
    Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking {
            authDataStoreRepository.authFlow.first().accessToken
        }

        var request = chain.request()
        request = request.newBuilder().header("Authorization", "bearer $accessToken")
            .build()
        return chain.proceed(request)
    }


}