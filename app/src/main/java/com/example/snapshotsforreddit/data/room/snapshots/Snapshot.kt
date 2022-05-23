package com.example.snapshotsforreddit.data.room.snapshots

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat


@Entity(tableName = "snapshot_database")
@Parcelize
data class Snapshot(

    @ColumnInfo(name = "title")
    val title: String,

    val subreddit: String,

    @ColumnInfo(name = "permalink")
    val permalink: String,

    val important: Boolean = false,

    //list of comment objects

    val added: Long = System.currentTimeMillis(),

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

): Parcelable {
    val addedDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(added)



}