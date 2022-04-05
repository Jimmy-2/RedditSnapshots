package com.example.snapshotsforreddit.network.services

import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedJsonResponse
import com.example.snapshotsforreddit.network.responses.subscribed.Username
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface RedditApiService {
    companion object {
        const val OAUTH_URL = "https://oauth.reddit.com"

    }

    @GET("/api/v1/me")
    suspend fun getLoggedInUsername(
        //@Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?
    ): Username



    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribedList(
        //@Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Query("after") after: String? = null
    ): SubscribedJsonResponse


    @GET("/{r}/{subreddit}/{sort}")
    suspend fun getListOfPosts(
       // @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("r") type: String,
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: String?,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): RedditJsonResponse

}