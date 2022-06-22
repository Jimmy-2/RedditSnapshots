package com.example.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RedditPagePostDao {
    @Query("SELECT * FROM reddit_page_posts")
    fun getRedditPagePosts(): Flow<List<RedditPagePost>>

    //overwrite results with same primary keys (if object with same primary key exists, it will be replaced)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRedditPagePosts(redditPagePost: List<RedditPagePost>)

    //replace and delete previous list of posts
    @Query("DELETE FROM reddit_page_posts")
    suspend fun deleteRedditPagePostsForRefresh()
}