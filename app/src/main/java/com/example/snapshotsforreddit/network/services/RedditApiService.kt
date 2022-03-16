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

interface RedditApiService {
    @POST("/api/v1/access_token")
    @FormUrlEncoded
    fun getToken(
        @Header("User-Agent") User_Agent: String?,
        @Header("Authorization") Authorization: String?,
        @Field("grant_type") grant_type: String?,
        @Field("code") code: String?,
        @Field("redirect_uri") redirect_uri: String?
    ): Call<TokenResponse>

    //use moreChildren endpoint to get more posts after the last one
    @GET("/?limit=100")
    fun getListPost(
        @Header("Authorization") Authorization: String?,
        @Header("User-Agent") User_Agent: String?
    ): Call<RedditJsonResponse>

    //testing method for test_url
    @GET("/")
    fun getListPost2(): Call<RedditJsonResponse>

}



object RedditApi {
    val retrofitServiceToken: RedditApiService by lazy {
        retrofitGetToken.create(RedditApiService::class.java)
    }

    val retrofitServiceOAuth: RedditApiService by lazy {
        retrofitOAuth.create()
    }


}
