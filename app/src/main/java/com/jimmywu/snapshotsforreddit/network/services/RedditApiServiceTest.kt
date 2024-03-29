package com.jimmywu.snapshotsforreddit.network.services

import com.jimmywu.snapshotsforreddit.network.responses.RedditJsonResponse
import com.jimmywu.snapshotsforreddit.network.responses.TokenResponse
import com.jimmywu.snapshotsforreddit.network.responses.account.User
import com.jimmywu.snapshotsforreddit.network.responses.account.Username
import com.jimmywu.snapshotsforreddit.network.responses.redditpostdetails.RedditPostCommentsDetail
import com.jimmywu.snapshotsforreddit.network.responses.redditpostdetails.RepliesAdapter
import com.jimmywu.snapshotsforreddit.network.responses.subreddit.SubredditJsonResponse
import com.jimmywu.snapshotsforreddit.network.services.RedditApiServiceTest.Companion.retrofitGetToken
import com.jimmywu.snapshotsforreddit.network.services.RedditApiServiceTest.Companion.retrofitOAuth
import com.jimmywu.snapshotsforreddit.network.services.RedditApiServiceTest.Companion.retrofitTest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.*





interface RedditApiServiceTest {
    companion object {
        private const val API_URL = "https://www.reddit.com"

        private const val OAUTH_URL = "https://oauth.reddit.com"

        //test getting json objects from front page first
        private const val TEST_URL = "https://api.reddit.com"

//        private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val moshi = Moshi.Builder().add(RepliesAdapter()).addLast(KotlinJsonAdapterFactory()).build()


        val retrofitGetToken = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(API_URL).build()

        val retrofitOAuth = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(OAUTH_URL).build()

        val retrofitTest = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(TEST_URL).build()
    }


    @FormUrlEncoded
    @POST("/api/v1/access_token")
    fun getToken(
        @Header("User-Agent") User_Agent: String?,
        @Header("Authorization") Authorization: String?,
        @Field("grant_type") grant_type: String?,
        @Field("code") code: String?,
        @Field("redirect_uri") redirect_uri: String?
    ): Call<TokenResponse>




    //use moreChildren endpoint to get more posts after the last one
    @GET("/r/popular")
    fun getListOfPosts(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?
    ): Call<RedditJsonResponse>

    @GET("/r/popular")
    fun getListOfPostsTest(
        @Query("after") after: String?
    ): Call<RedditJsonResponse>



    @GET("/r/{subreddit}/comments/{id}/")
    fun getPostDetails(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?,
        @Path("subreddit") permalink: String?,
        @Path("id") id: String?
    ): Call<List<RedditJsonResponse>>
    //post detail will contain a list of redditjsonresponses of size 2

    @GET("/r/{subreddit}/comments/{id}/")
    fun getPostDetailsTest(
        @Path("subreddit") permalink: String?,
        @Path("id") id: String?
    ): Call<List<RedditJsonResponse>>



    @GET("/api/v1/me")
    fun getLoggedInUsername(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?
    ): Call<Username>

    @GET("/subreddits/mine/subscriber")
    fun getSubscribedList(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?
    ): Call<SubredditJsonResponse>

    @GET("/user/{username}/about")
    fun getUserInfoTest(
        @Path("username") username: String
    ): Call<User>

    @GET("/{r}/{subreddit}/search/")
    fun getSearchResultsTest(
        // @Header("Authorization") Authorization: String?,
        @Path("r") type: String,
        @Path("subreddit") subreddit: String,
        @Query("q") q: String?,

        @Query("restrict_sr") restrict_sr: Int,
        @Query("sr_nsfw") sr_nsfw: String?,
        @Query("sort") sort: String?,

        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
        @Query("sr_detail") sr_detail: Int? = 1
    ): Call<RedditJsonResponse>

    @GET("/subreddits/search")
    fun getSearchResultsSubreddit(
        @Query("q") q: String?,
        @Query("type") type: String?,
        @Query("include_over_18") include_over_18r: Int?,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null,
    ): Call<SubredditJsonResponse>


    @GET("/r/{subreddit}/comments/{id}")
    fun getPostComments(
        @Path("subreddit") subreddit: String,
        @Path("id") id: String,
    ): Call<List<RedditPostCommentsDetail>>


}



object RedditApiTest {
    val RETROFIT_SERVICE_TEST_TOKEN: RedditApiServiceTest by lazy {
        retrofitGetToken.create(RedditApiServiceTest::class.java)
    }

    val RETROFIT_SERVICE_TEST_O_AUTH: RedditApiServiceTest by lazy {
        retrofitOAuth.create()
    }

    val RETROFIT_SERVICE_TEST_TEST: RedditApiServiceTest by lazy {
        retrofitTest.create()
    }


}
