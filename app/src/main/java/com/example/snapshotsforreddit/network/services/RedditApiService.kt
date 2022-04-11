package com.example.snapshotsforreddit.network.services

import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedJsonResponse
import com.example.snapshotsforreddit.network.responses.subscribed.Username
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/user/{username}/{historyType}")
    suspend fun getUserHistory(
        // @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("username") username: String,
        @Path("historyType") historyType: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): RedditJsonResponse

}