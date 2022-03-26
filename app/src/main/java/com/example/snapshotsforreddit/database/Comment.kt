package com.example.snapshotsforreddit.database

import android.os.Parcelable

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "comment_database")
@Parcelize
data class Comment (

    val parentPost: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

): Parcelable {




}