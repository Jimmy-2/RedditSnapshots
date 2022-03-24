package com.example.snapshotsforreddit.network.services

import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.*

private const val API_URL = "https://www.reddit.com"

private const val OAUTH_URL = "https://oauth.reddit.com"

//test getting json objects from front page first
private const val TEST_URL = "https://api.reddit.com"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


private val retrofitGetToken = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(API_URL).build()

private val retrofitOAuth = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(OAUTH_URL).build()

private val retrofitTest = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(TEST_URL).build()



interface RedditApiService {
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
    fun getListOfPostsTest(): Call<RedditJsonResponse>



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

}



object RedditApi {
    val retrofitServiceToken: RedditApiService by lazy {
        retrofitGetToken.create(RedditApiService::class.java)
    }

    val retrofitServiceOAuth: RedditApiService by lazy {
        retrofitOAuth.create()
    }

    val retrofitServiceTest: RedditApiService by lazy {
        retrofitTest.create()
    }


}
