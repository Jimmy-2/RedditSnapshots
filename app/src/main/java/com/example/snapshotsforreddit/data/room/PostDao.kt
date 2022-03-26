package com.example.snapshotsforreddit.data.room


import androidx.room.*
import com.example.snapshotsforreddit.data.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    fun getDownloadedPosts(query: String, sortOrder: SortOrder): Flow<List<Post>> =
        when(sortOrder) {
            SortOrder.BY_TITLE -> getDownloadedPostsSortedByTitle(query)
            SortOrder.BY_DATE -> getDownloadedPostsSortedByDateAdded(query)
            SortOrder.BY_SUBREDDIT -> getDownloadedPostsSortedBySubreddit(query)
        }

    //removing the '%' || means our filtered results must start with searchquery
    @Query("SELECT * FROM post_database WHERE title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, title")
    fun getDownloadedPostsSortedByTitle(searchQuery: String): Flow<List<Post>>

    @Query("SELECT * FROM post_database WHERE title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, added")
    fun getDownloadedPostsSortedByDateAdded(searchQuery: String): Flow<List<Post>>

    @Query("SELECT * FROM post_database WHERE title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, subreddit")
    fun getDownloadedPostsSortedBySubreddit(searchQuery: String): Flow<List<Post>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)



}