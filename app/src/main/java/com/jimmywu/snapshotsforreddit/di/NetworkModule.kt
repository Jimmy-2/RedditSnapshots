package com.jimmywu.snapshotsforreddit.di


import com.jimmywu.snapshotsforreddit.network.AuthApiAuthenticator
import com.jimmywu.snapshotsforreddit.network.RedditApiInterceptor
import com.jimmywu.snapshotsforreddit.network.services.RedditApiService
import com.jimmywu.snapshotsforreddit.network.services.RedditAuthApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //Authentication
    @Provides
    @Singleton
    @Named("Auth")
    fun provideAuthRetroFit(): Retrofit =
        Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(
                    KotlinJsonAdapterFactory()
                ).build()
            )
        ).baseUrl(
            RedditAuthApiService.BASE_URL
        ).build()

    @Provides
    @Singleton
    fun provideRedditAuthApiService(@Named("Auth") retrofit: Retrofit): RedditAuthApiService =
        retrofit.create(RedditAuthApiService::class.java)


    //Regular reddit usage
    @Provides
    @Singleton
    @Named("Regular")
    fun provideRedditRetroFit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(
                    KotlinJsonAdapterFactory()
                ).build()
            )
        ).baseUrl(
            RedditApiService.OAUTH_URL
        ).client(okHttpClient).build()

    @Provides
    @Singleton
    fun provideRedditApiService(@Named("Regular") retrofit: Retrofit): RedditApiService =
        retrofit.create()


    @Provides
    @Singleton
    fun provideOkHttpClient(
        redditAPiInterceptor: RedditApiInterceptor,
        authApiAuthenticator: AuthApiAuthenticator
    ): OkHttpClient {
        //val logging = HttpLoggingInterceptor()
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(redditAPiInterceptor)
            //.addInterceptor(logging)
            .authenticator(authApiAuthenticator)
            .build()
    }

}