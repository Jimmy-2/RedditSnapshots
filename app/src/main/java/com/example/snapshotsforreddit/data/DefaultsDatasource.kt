package com.example.snapshotsforreddit.data

import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.network.responses.account.UserData
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenData
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject

class DefaultsDatasource {
    fun loadDefaultSubreddits(): List<SubscribedChildrenObject> {
        return listOf(
            SubscribedChildrenObject(
                kind = null,
                data = SubscribedChildrenData("Home", "Home", null, null, "", true)
            ),
            SubscribedChildrenObject(
                kind = null,
                data = SubscribedChildrenData("Popular", "r/Popular", null, null, "r", true)
            ),
            SubscribedChildrenObject(
                kind = null,
                data = SubscribedChildrenData("All", "r/All", null, null, "r", true)
            ),
        )
    }

    fun emptyRedditChildrenData(kind: String?, userData: UserData? ) : List<RedditChildrenObject> {
        return listOf(RedditChildrenObject(
            kind = "userData",
            data = RedditChildrenData(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null, null, userData
            )
        ))
    }

    fun loadDefaultAccountItems(userData: UserData?): List<RedditChildrenObject> {
        return listOf(
            RedditChildrenObject(
                kind = "userData",
                data = RedditChildrenData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null, null, userData
                )
            ),
            RedditChildrenObject(
                kind = "defaultTop",
                data = RedditChildrenData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "submitted",
                    null,
                    "Posts", null, null
                )
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "comments",
                    null,
                    "Comments", null,null
                )
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "saved",
                    null,
                    "Saved", null,null
                )
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Friends", null,null
                )
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "upvoted",
                    null,
                    "Upvoted", null,null
                )
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "downvoted",
                    null,
                    "Downvoted", null,null
                )
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "hidden",
                    null,
                    "Hidden",
                    null,null
                )
            ),
            RedditChildrenObject(
                kind = "defaultBottom",
                data = RedditChildrenData(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Trophies",
                    null,null
                )
            ),


            )
    }
}