package com.example.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.room.Entity

@Entity(tableName = "reddit_page_remote_keys", primaryKeys = ["redditPageName","redditPageSortOrder"])
data class RedditPageNameRemoteKey(

    val redditPageName: String,
    val redditPageSortOrder: String,

    val nextPageKey: String?
)
