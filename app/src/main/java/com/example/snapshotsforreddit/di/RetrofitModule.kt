package com.example.snapshotsforreddit.di

import com.example.snapshotsforreddit.network.services.RedditApi
import com.example.snapshotsforreddit.network.services.RedditAuthApi
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
            RedditAuthApi.BASE_URL
        ).build()

    @Provides
    @Singleton
    fun provideRedditAuthApi(@Named("Auth") retrofit: Retrofit): RedditAuthApi =
        retrofit.create(RedditAuthApi::class.java)





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
            RedditApi.OAUTH_URL
        ).build()


    @Provides
    @Singleton
    fun provideRedditApi(@Named("Regular") retrofit: Retrofit): RedditApi =
        retrofit.create()


}