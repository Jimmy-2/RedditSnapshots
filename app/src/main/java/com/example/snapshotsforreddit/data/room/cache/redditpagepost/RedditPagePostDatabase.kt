package com.example.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RedditPagePost::class], version = 2)
abstract class RedditPagePostDatabase: RoomDatabase()  {
    abstract fun redditPagePostDao(): RedditPagePostDao
}