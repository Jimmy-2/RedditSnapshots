package com.example.snapshotsforreddit.data.room.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.snapshotsforreddit.data.room.snapshots.Snapshot
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import kotlinx.coroutines.flow.Flow

@Dao
interface RedditChildrenObjectDao {

    @Query("SELECT * FROM redditChildrenObjects")
    fun getRedditChildrenObjects(): Flow<List<RedditChildrenObject>>

    //overwrite results with same primary keys
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRedditChildrenObjects(redditChildrenObjects: List<RedditChildrenObject>)

    @Query("DELETE FROM redditChildrenObjects")
    suspend fun deleteAllRedditChildrenObjects(snapshot: Snapshot)


}