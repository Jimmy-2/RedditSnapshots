package com.example.snapshotsforreddit.di

import android.content.Context
import com.example.snapshotsforreddit.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//global module
//singleton


//scoped to the application. live as long as the app is working

object AppModule {

    //life time is as long as app is alive

    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }


}