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
                kind = "default",
                data = SubscribedChildrenData("Home", "Home", null, null, null, true, "Posts from your subscribed subreddits")
            ),
            SubscribedChildrenObject(
                kind = "default",
                data = SubscribedChildrenData("Popular Posts", "r/Popular", null, null, "r", true, "Popular recommended posts from Reddit")
            ),
            SubscribedChildrenObject(
                kind = "default",
                data = SubscribedChildrenData("All Posts", "r/All", null, null, "r", true, "Most active posts from all of Reddit")
            ),
        )
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