package com.example.snapshotsforreddit.data.room.cache.subscribedsubreddit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface SubscribedSubredditDao {

//    @Query("SELECT * FROM subscribed_subreddits ORDER BY display_name ASC")
    @Query("SELECT * FROM subscribed_subreddits ORDER BY display_name COLLATE NOCASE ASC")
    fun getSubscribedSubreddits(): Flow<List<SubscribedSubreddit>>

    //overwrite results with same primary keys (if object with same primary key exists, it will be replaced)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscribedSubreddits(subscribedSubreddit: List<SubscribedSubreddit>)

    //replace and delete previous list of subscribed subreddits
    @Query("DELETE FROM subscribed_subreddits")
    suspend fun deleteSubscribedSubredditsForRefresh()


}