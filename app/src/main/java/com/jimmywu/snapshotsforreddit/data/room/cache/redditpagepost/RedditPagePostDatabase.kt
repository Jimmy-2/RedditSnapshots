package com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RedditPagePost::class, RedditPageNameRemoteKey::class], version = 1)

abstract class RedditPagePostDatabase: RoomDatabase()  {

    abstract fun redditPagePostDao(): RedditPagePostDao

    abstract fun redditPageNameRemoteKeyDao(): RedditPageNameRemoteKeyDao
}