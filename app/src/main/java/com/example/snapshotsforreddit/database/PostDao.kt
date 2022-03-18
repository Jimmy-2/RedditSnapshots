package com.example.snapshotsforreddit.database


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)

    //use @Query annotation for specific functionalities
    @Query("SELECT * from post ORDER BY link ASC")
    fun getSavedPosts(): Flow<List<Post>>

}