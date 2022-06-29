package com.example.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "reddit_page_remote_keys", primaryKeys = ["redditPageName","redditPageSortOrder"])
//data class RedditPageNameRemoteKey(
//
//    val redditPageName: String,
//    val redditPageSortOrder: String,
//
//    val nextPageKey: String?
//)


@Entity(tableName = "reddit_page_remote_keys")
data class RedditPageNameRemoteKey(

    @PrimaryKey
    val redditPageNameAndSortOrder: String,

    val nextPageKey: String?
)

