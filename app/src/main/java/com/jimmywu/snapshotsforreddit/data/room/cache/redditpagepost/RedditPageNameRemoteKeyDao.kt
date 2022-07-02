package com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RedditPageNameRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: RedditPageNameRemoteKey)

    @Query("SELECT * FROM reddit_page_remote_keys WHERE redditPageNameAndSortOrder = :redditPageNameAndSortOrder")
    suspend fun getRemoteKeys(redditPageNameAndSortOrder: String): RedditPageNameRemoteKey

    @Query("DELETE FROM reddit_page_remote_keys WHERE redditPageNameAndSortOrder = :redditPageNameAndSortOrder")
    suspend fun deleteRemoteKeys(redditPageNameAndSortOrder: String)
}