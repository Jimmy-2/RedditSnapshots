package com.example.snapshotsforreddit.network.services

import com.example.snapshotsforreddit.network.responses.PostRequestResponse
import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.responses.account.User
import com.example.snapshotsforreddit.network.responses.account.Username
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditJsonResponse
import retrofit2.http.*

interface RedditApiService {
    companion object {
        const val OAUTH_URL = "https://oauth.reddit.com"

    }

    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribedSubreddits(
        //@Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): SubredditJsonResponse

    @GET("/{sort}/?sr_detail=1")
    suspend fun getHomePosts(
        // @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("sort") sort: String?,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): RedditJsonResponse


    @GET("/{r}/{subreddit}/{sort}/?sr_detail=1")
    suspend fun getSubredditPosts(
        // @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("r") pageType: String,
        @Path("subreddit") subreddit: String,
        @Path("sort") sort: String?,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): RedditJsonResponse


    @GET("/{r}/{subreddit}/search")
    suspend fun getSearchResults(
        // @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("r") pageType: String,
        @Path("subreddit") subreddit: String,
        @Query("q") q: String?
        ,
        @Query("restrict_sr") restrict_sr: Int,
        @Query("sr_nsfw") sr_nsfw: String?,
        @Query("sort") sort: String?,
        @Query("type") type: String?,

        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,

        @Query("sr_detail") sr_detail: Int?
    ): RedditJsonResponse

    @GET("/subreddits/search")
    suspend fun getSearchResultsSubreddit(
        @Header("User-Agent") User_Agent: String?,
        @Query("q") q: String?,
        @Query("type") type: String?,
        @Query("include_over_18") include_over_18r: Int?,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): SubredditJsonResponse



    @GET("/api/v1/me")
    suspend fun getLoggedInUsername(
        //@Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?
    ): Username


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

    @GET("/subreddits/search")
    suspend fun getSubredditByQuery(
        // @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("q") q: String
    ): User
    /*
    after
fullname of a thing

before
fullname of a thing

count
a positive integer (default: 0)

limit
the maximum number of items desired (default: 25, maximum: 100)

q
a search query

search_query_id
a uuid

show
(optional) the string all

show_users
boolean value

sort
one of (relevance, activity)

sr_detail
(optional) expand subreddits

typeahead_active
boolean value or None
     */


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