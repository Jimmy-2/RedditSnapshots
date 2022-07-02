package com.jimmywu.snapshotsforreddit.network


import android.util.Base64
import com.jimmywu.snapshotsforreddit.BuildConfig
import com.jimmywu.snapshotsforreddit.data.datastore.AuthDataStoreRepository
import com.jimmywu.snapshotsforreddit.network.services.RedditAuthApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthApiRepository @Inject constructor(
    private val redditAuthApiService: RedditAuthApiService,
    private val authDataStoreRepository: AuthDataStoreRepository
) {
    suspend fun getTokenValues(code: String) =
        redditAuthApiService.getTokens(
            USER_AGENT,
            "Basic $encodedAuthString:",
            GRANT_TYPE,
            code,
            REDIRECT_URI
        )

    suspend fun logoutUser(token: String, tokenType: String) =
        redditAuthApiService.revokeTokens(USER_AGENT, "Basic $encodedAuthString:", token, tokenType)


    fun getNewAccessToken(refreshToken: String) =
        redditAuthApiService.getRefreshedAccessToken(
            USER_AGENT,
            "Basic $encodedAuthString:",
            REFRESH_GRANT_TYPE,
            refreshToken
        )


    companion object {
        private val encodedAuthString: String by lazy {
            Base64.encodeToString(
                AUTH_STRING.toByteArray(),
                Base64.NO_WRAP
            )
        }

        private const val AUTH_STRING = "${BuildConfig.REDDIT_CLIENT_ID}:"
        private const val USER_AGENT = BuildConfig.USER_AGENT
        private const val GRANT_TYPE = "authorization_code"
        private const val REFRESH_GRANT_TYPE = "refresh_token"
        private const val REDIRECT_URI = BuildConfig.AUTH_REDIRECT_URI
    }

}
