package com.example.snapshotsforreddit.network.services

import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedJsonResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface RedditApi {
    companion object {
        const val CLIENT_ID = BuildConfig.REDDIT_CLIENT_ID
        const val OAUTH_URL = "https://oauth.reddit.com"

    }


    //@Headers("User-Agent: Testing", "Authorization: bearer $CLIENT_ID")
    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribedList(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): SubscribedJsonResponse



    @GET("/r/popular")
    suspend fun getListOfPosts(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): RedditJsonResponse

}