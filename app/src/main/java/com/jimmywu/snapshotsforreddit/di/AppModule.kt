package com.jimmywu.snapshotsforreddit.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

//give dagger instructions on how to create the dependencies that we need
//scoped to the application. live as long as the app is working
@Module
@InstallIn(SingletonComponent::class)
//refactor/rename to database module
object AppModule {
    //life time is as long as app is alive

    //we only ever need 1 instance of our dao and database throughout the app
    //so we annotate with singleton
    @ApplicationScope
    @Provides
    @Singleton
    //provide a coroutine scope that lives as long as the application is running
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
    //supervisorjob prevents scope from cancelling when one of its children(operation) fails



}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope