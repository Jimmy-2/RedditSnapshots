package com.example.snapshotsforreddit.di

import android.app.Application
import androidx.room.Room
import com.example.snapshotsforreddit.data.room.AccountRoomDatabase
import com.example.snapshotsforreddit.data.room.PostRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    //dagger will create and provide the post database
    @Provides
    @Singleton
    fun providePostDatabase(
        app: Application,
        callback: PostRoomDatabase.Callback

    ) = Room.databaseBuilder(app, PostRoomDatabase::class.java, "post_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    //create the actual post dao object we need to make database operation
    @Provides
    @Singleton
    fun providePostDao(db: PostRoomDatabase) = db.postDao()



    @Provides
    @Singleton
    fun provideAccountDatabase(
        app: Application,
        callback: AccountRoomDatabase.Callback
    ) = Room.databaseBuilder(app, AccountRoomDatabase::class.java, "account_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    @Singleton
    fun provideAccountDao(db: AccountRoomDatabase) = db.accountDao()


}