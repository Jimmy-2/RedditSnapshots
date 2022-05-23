package com.example.snapshotsforreddit.network


import com.example.snapshotsforreddit.data.datastore.AuthDataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthApiAuthenticator @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking { authDataStoreRepository.authFlow.first().refreshToken }

        try {
            val refreshTokenResponse = authApiRepository.getNewAccessToken(refreshToken).execute()
            val data = refreshTokenResponse.body()
            if (data?.access_token != null) {
                val newAccessToken = data.access_token
                // save new access token into the datastore
                runBlocking { authDataStoreRepository.updateAccessToken(newAccessToken) }
                // retry request with the new access token
                return response.request.newBuilder()
                    .header("Authorization", "bearer $newAccessToken").build()
            } else {
                throw HttpException(refreshTokenResponse)
            }
        } catch (throwable: Throwable) {

        }
        runBlocking { authDataStoreRepository.updateLoginState(false) }
        return null

    }


}

