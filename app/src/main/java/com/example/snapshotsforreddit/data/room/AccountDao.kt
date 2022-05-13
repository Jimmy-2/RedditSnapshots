package com.example.snapshotsforreddit.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account_database")
    fun getLoggedInAccounts(): Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: Account)

    @Delete
    suspend fun delete(account: Account)
}

