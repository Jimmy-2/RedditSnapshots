package com.example.snapshotsforreddit.di

import com.example.snapshotsforreddit.network.services.RedditApiService
import com.example.snapshotsforreddit.network.services.RedditAuthApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

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
    fun provideRedditAuthApi(@Named("Auth") retrofit: Retrofit): RedditAuthApiService =
        retrofit.create(RedditAuthApiService::class.java)




    //Regular reddit usage
    @Provides
    @Singleton
    @Named("Regular")
    fun provideRedditRetroFit(): Retrofit =
        Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(
                    KotlinJsonAdapterFactory()
                ).build()
            )
        ).baseUrl(
            RedditApiService.OAUTH_URL
        ).build()


    @Provides
    @Singleton
    fun provideRedditApi(@Named("Regular") retrofit: Retrofit): RedditApiService =
        retrofit.create()


}