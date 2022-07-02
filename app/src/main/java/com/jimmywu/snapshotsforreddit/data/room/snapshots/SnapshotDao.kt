package com.jimmywu.snapshotsforreddit.data.room.snapshots


import androidx.room.*
import com.jimmywu.snapshotsforreddit.data.datastore.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface SnapshotDao {

    fun getSnapshots(query: String, sortOrder: SortOrder): Flow<List<Snapshot>> =
        when(sortOrder) {
            SortOrder.BY_TITLE -> getSnapshotsSortedByTitle(query)
            SortOrder.BY_DATE -> getSnapshotsSortedByDateAdded(query)
            SortOrder.BY_SUBREDDIT -> getSnapshotsSortedBySubreddit(query)
        }

    //removing the '%' || means our filtered results must start with searchquery
    @Query("SELECT * FROM snapshot_database WHERE title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, title")
    fun getSnapshotsSortedByTitle(searchQuery: String): Flow<List<Snapshot>>

    @Query("SELECT * FROM snapshot_database WHERE title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, added")
    fun getSnapshotsSortedByDateAdded(searchQuery: String): Flow<List<Snapshot>>

    @Query("SELECT * FROM snapshot_database WHERE title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, subreddit")
    fun getSnapshotsSortedBySubreddit(searchQuery: String): Flow<List<Snapshot>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(snapshot: Snapshot)

    @Delete
    suspend fun delete(snapshot: Snapshot)



}