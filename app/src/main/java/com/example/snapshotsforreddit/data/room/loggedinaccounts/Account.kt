package com.example.snapshotsforreddit.data.room.loggedinaccounts

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "account_database")
@Parcelize
data class Account(
    val username: String,

    val refreshToken: String,

    val accessToken: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

) : Parcelable