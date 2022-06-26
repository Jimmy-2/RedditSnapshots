package com.example.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface RedditPagePostDao {

    @Query("SELECT * FROM reddit_page_posts WHERE redditPageName = :redditPageName AND redditPageSortOrder = :sortOrder")
    fun getRedditPagePosts(redditPageName: String, sortOrder: String): PagingSource<Int, RedditPagePost>

    //overwrite results with same primary keys (if object with same primary key exists, it will be replaced)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRedditPagePosts(redditPagePost: List<RedditPagePost>)

    //replace and delete previous list of posts
    @Query("DELETE FROM reddit_page_posts WHERE redditPageName = :redditPageName AND redditPageSortOrder = :sortOrder")
    suspend fun deleteRedditPagePostsForRefresh(redditPageName: String, sortOrder: String)

//    @Query("SELECT MAX(position) FROM reddit_page_posts WHERE redditPageName = :redditPageName AND redditPageSortOrder = :sortOrder")
//    suspend fun getLastPosition(redditPageName: String, sortOrder: String): Int?

    @Update
    suspend fun updateRedditPagePost(RedditPost: RedditPagePost)
}