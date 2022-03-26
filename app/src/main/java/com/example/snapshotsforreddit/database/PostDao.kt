package com.example.snapshotsforreddit.database


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    //use @Query annotation for specific functionalities
    @Query("SELECT * FROM post_database")
    fun getDownloadedPosts(): Flow<List<Post>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)



}