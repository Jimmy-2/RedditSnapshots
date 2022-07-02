package com.jimmywu.snapshotsforreddit.data.room.loggedinaccounts

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

    @Query("UPDATE account_database SET refreshToken=:newRefreshToken WHERE username = :username")
    suspend fun updateLoggedIn(newRefreshToken: String, username: String)

    @Query("SELECT EXISTS (SELECT 1 FROM account_database WHERE username = :username)")
    suspend fun exists(username: String): Boolean

    @Query("DELETE FROM account_database WHERE username = :username")
    suspend fun deleteAccount(username: String)
}

