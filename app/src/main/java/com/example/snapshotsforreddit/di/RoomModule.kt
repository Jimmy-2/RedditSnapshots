package com.example.snapshotsforreddit.di

import android.app.Application
import androidx.room.Room
import com.example.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePostDatabase
import com.example.snapshotsforreddit.data.room.cache.subscribedsubreddit.SubscribedSubredditDatabase
import com.example.snapshotsforreddit.data.room.loggedinaccounts.AccountRoomDatabase
import com.example.snapshotsforreddit.data.room.snapshots.SnapshotRoomDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    //snapshots
    //dagger will create and provide the snapshot database
    @Provides
    @Singleton
    fun provideSnapshotDatabase(
        app: Application,
        callback: SnapshotRoomDatabase.Callback

    ) = Room.databaseBuilder(app, SnapshotRoomDatabase::class.java, "snapshot_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    //create the actual snapshot dao object we need to make database operation
    @Provides
    @Singleton
    fun provideSnapshotDao(db: SnapshotRoomDatabase) = db.snapshotDao()




    // Accounts
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


//    // Posts caching
//    @Provides
//    @Singleton
//    fun provideRedditChildrenObjectDatabase(app: Application): RedditChildrenObjectDatabase =
//        Room.databaseBuilder(app, RedditChildrenObjectDatabase::class.java, "redditChildrenObject_database")
//        .build()

//    @Provides
//    @Singleton
//    fun provideRedditChildrenObjectDao(db: RedditChildrenObjectDatabase) = db.redditChildrenObjectDao()


    //Caching

    @Provides
    @Singleton
    fun provideSubscribedSubredditsDatabase(app: Application): SubscribedSubredditDatabase =
        Room.databaseBuilder(app, SubscribedSubredditDatabase::class.java, "subscribed_subreddits_database")
        .fallbackToDestructiveMigration()
        .build()



    @Provides
    @Singleton
    fun provideRedditPagePostsDatabase(app: Application): RedditPagePostDatabase =
        Room.databaseBuilder(app, RedditPagePostDatabase::class.java, "reddit_page_posts_database")
            .fallbackToDestructiveMigration()
            .build()


}