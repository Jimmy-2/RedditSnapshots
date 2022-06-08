package com.example.snapshotsforreddit.data.room.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData
import kotlinx.coroutines.flow.Flow


@Dao
interface SubscribedSubredditDao {
    @Query("SELECT * FROM subscribed_subreddits")
    fun getSubscribedSubreddits(): Flow<List<SubredditChildrenData>>

    //overwrite results with same primary keys
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscribedSubreddits(subscribedSubredditData: List<SubredditChildrenData>)

    //replace and delete previous list of subscribed subreddits
    @Query("DELETE FROM subscribed_subreddits")
    suspend fun deleteSubscribedSubredditsForRefresh()

}