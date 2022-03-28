package com.example.snapshotsforreddit.data.Room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat


@Entity(tableName = "post_database")
@Parcelize
data class Post(

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