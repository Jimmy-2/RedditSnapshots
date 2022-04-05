package com.example.snapshotsforreddit.network.services

import com.example.snapshotsforreddit.network.responses.TokenResponse
import retrofit2.Call
import retrofit2.http.*


interface RedditAuthApiService {
    companion object {
        const val BASE_URL = "https://www.reddit.com"
    }

    @FormUrlEncoded
    //@Headers("User-Agent: $USER_AGENT", "Authorization: Basic $encodedAuthString")
    @POST("/api/v1/access_token")
    suspend fun getTokens(
        @Header("User-Agent") User_Agent: String?,
        @Header("Authorization") Authorization: String?,
        @Field("grant_type") grant_type: String?,
        @Field("code") code: String?,
        @Field("redirect_uri") redirect_uri: String?
    ): TokenResponse

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    fun getRefreshedAccessToken(
        @Header("User-Agent") User_Agent: String?,
        @Header("Authorization") Authorization: String?,
        @Field("grant_type") grant_type: String?,
        @Field("refresh_token") refresh_token: String?,
    ): Call<TokenResponse>



}