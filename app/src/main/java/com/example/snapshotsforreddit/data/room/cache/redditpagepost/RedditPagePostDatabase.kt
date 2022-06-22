package com.example.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.room.Database

@Database(entities = [RedditPagePost::class], version = 1)
abstract class RedditPagePostDatabase {
    abstract fun redditPagePostDao(): RedditPagePostDao
}