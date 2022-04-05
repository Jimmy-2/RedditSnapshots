package com.example.snapshotsforreddit.network

import androidx.lifecycle.asLiveData
import com.example.snapshotsforreddit.data.Repository.AuthApiRepository
import com.example.snapshotsforreddit.data.Repository.AuthDataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


/*
class AuthApiInterceptor(private val tokenProvider: AuthApiRepository): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = tokenProvider.getTokenValues()

        return if (accessToken == null) {
            chain.proceed(chain.request())
        } else {
            val authenticatedRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", "bearer $accessToken")
                .build()
            chain.proceed(authenticatedRequest)

        }


    }
}

 */

@Singleton
class RedditApiInterceptor @Inject constructor(private val authDataStoreRepository: AuthDataStoreRepository):Interceptor{
    //val getRefreshToken =runBlocking { session.getRefreshToken() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val getRefreshToken = runBlocking { authDataStoreRepository.getValsFromPreferencesStore().first().accessToken }
        var request = chain.request()
        request = request.newBuilder().header("Authorization","bearer $getRefreshToken")
            .build()

        return chain.proceed(request)


    }


}