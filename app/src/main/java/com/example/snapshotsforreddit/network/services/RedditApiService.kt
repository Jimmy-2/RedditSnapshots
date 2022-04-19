package com.example.snapshotsforreddit.network.services

import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.responses.account.User
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedJsonResponse
import com.example.snapshotsforreddit.network.responses.account.Username
import com.example.snapshotsforreddit.network.responses.subscribed.PostRequestResponse
import retrofit2.Call
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


    @GET("/{r}/{subreddit}/{sort}/?sr_detail=1")
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

    @GET("/{sort}/?sr_detail=1")
    suspend fun getListOfHomePosts(
        // @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("sort") sort: String?,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): RedditJsonResponse


    @GET("/user/{username}/{historyType}/?sr_detail=1")
    suspend fun getUserHistory(
        // @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("username") username: String,
        @Path("historyType") historyType: String,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): RedditJsonResponse


    @GET("/user/{username}/about")
    suspend fun getUserInfo(
        // @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("username") username: String
    ): User


//    POST requests
    @FormUrlEncoded
    @POST("/api/vote")
    suspend fun vote(
        @Header("User-Agent") User_Agent: String?,
        @Field("dir") dir: Int,
        @Field("id") id: String,
    ): PostRequestResponse

    @FormUrlEncoded
    @POST("/api/save")
    suspend fun save(
        @Header("User-Agent") User_Agent: String?,
        @Field("id") id: String,
    ): PostRequestResponse


    @FormUrlEncoded
    @POST("/api/unsave")
    suspend fun unsave(
        @Header("User-Agent") User_Agent: String?,
        @Field("id") id: String,
    ): PostRequestResponse


}