package com.example.snapshotsforreddit.database

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
    @ColumnInfo(name = "permalink")
    val permalink: String,

    val important: Boolean = false,

    val added: Long = System.currentTimeMillis(),

    //list of comment objects


    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

): Parcelable {
    val addedDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(added)



}