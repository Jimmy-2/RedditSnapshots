package com.example.snapshotsforreddit.network.services


import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


interface RedditAuthApi {
    companion object {

        const val CLIENT_ID = BuildConfig.REDDIT_CLIENT_ID
        const val BASE_URL = "https://www.reddit.com"

        private const val STATE = "random_string_to_check_if_correct_url"
    }

    @FormUrlEncoded
    @Headers("User-Agent Testing", "Authorization Basic $CLIENT_ID:")
    @POST("/api/v1/access_token")
    suspend fun getToken(
        @Header("User-Agent") User_Agent: String?,
        @Header("Authorization") Authorization: String?,
        @Field("grant_type") grant_type: String?,
        @Field("code") code: String?,
        @Field("redirect_uri") redirect_uri: String?
    ): Call<TokenResponse>



}