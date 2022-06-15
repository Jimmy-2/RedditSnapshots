package com.example.snapshotsforreddit.data.room.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SubscribedSubreddit::class], version = 1)
abstract class SubscribedSubredditDatabase: RoomDatabase()  {

    abstract fun subscribedSubredditDao(): SubscribedSubredditDao
}