package com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RedditPagePostDao {

    @Query("SELECT * FROM reddit_page_posts WHERE redditPageNameAndSortOrder = :redditPageNameAndSortOrder ORDER BY queryPosition ")
    fun getRedditPagePosts(redditPageNameAndSortOrder: String): PagingSource<Int, RedditPagePost>



    @Query("SELECT * FROM reddit_page_posts WHERE name = :name  AND redditPageNameAndSortOrder = :redditPageNameAndSortOrder ")
    fun getRedditPagePost(name: String, redditPageNameAndSortOrder: String): Flow<RedditPagePost>

    //overwrite results with same primary keys (if object with same primary key exists, it will be replaced)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRedditPagePosts(redditPagePost: List<RedditPagePost>)

    //replace and delete previous list of posts
    @Query("DELETE FROM reddit_page_posts WHERE redditPageNameAndSortOrder = :redditPageNameAndSortOrder")
    suspend fun deleteRedditPagePostsForRefresh(redditPageNameAndSortOrder: String)

    @Query("SELECT MAX(queryPosition) FROM reddit_page_posts WHERE redditPageNameAndSortOrder = :redditPageNameAndSortOrder")
    suspend fun getLastQueryPosition(redditPageNameAndSortOrder: String): Int?



//    @Query("SELECT MAX(position) FROM reddit_page_posts WHERE redditPageName = :redditPageName AND redditPageSortOrder = :sortOrder")
//    suspend fun getLastPosition(redditPageName: String, sortOrder: String): Int?

    //updates all stored post items and sets their isCompact values to the new value
    @Query("UPDATE reddit_page_posts SET isCompact = :isCompact")
    suspend fun updateRedditPageIsCompactLayout(isCompact: Boolean)


    @Update
    suspend fun updateRedditPagePost(RedditPost: RedditPagePost)
}