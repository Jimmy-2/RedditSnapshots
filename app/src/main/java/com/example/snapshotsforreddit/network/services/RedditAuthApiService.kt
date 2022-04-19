package com.example.snapshotsforreddit.network.services

import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.responses.subscribed.PostRequestResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


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


    //TODO use this in logout dialog fragment
    @FormUrlEncoded
    @POST("/api/v1/revoke_token")
    suspend fun revokeTokens(
        @Header("User-Agent") User_Agent: String?,
        @Header("Authorization") Authorization: String?,
        @Field("token") token: String?,
        @Field("token_type_hint") token_type_hint: String?,
    ): PostRequestResponse


}