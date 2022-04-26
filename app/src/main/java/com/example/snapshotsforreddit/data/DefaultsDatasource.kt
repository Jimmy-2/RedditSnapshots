package com.example.snapshotsforreddit.data

import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenObject
import com.example.snapshotsforreddit.network.responses.subreddit.SubscribedChildrenData

class DefaultsDatasource {
    fun loadDefaultSubreddits(): List<SubredditChildrenObject> {
        return listOf(
            SubredditChildrenObject(
                kind = "default",
                data = SubscribedChildrenData("Home", "Home", null,null,null, null, null, true, "Posts from your subscribed subreddits")
            ),
            SubredditChildrenObject(
                kind = "default",
                data = SubscribedChildrenData("Popular Posts", "r/Popular", null,null,null, null, "r", true, "Popular recommended posts from Reddit")
            ),
            SubredditChildrenObject(
                kind = "default",
                data = SubscribedChildrenData("All Posts", "r/All", null,null,null, null, "r", true, "Most active posts from all of Reddit")
            ),

            SubredditChildrenObject(
                kind = "header",
                data = null
            ),
        )
    }
    fun addHeader(): List<RedditChildrenObject> {
        return listOf(RedditChildrenObject(
            kind = "header",
            data = null
        ),)
    }

    fun addSearchBar(subreddit: String): List<RedditChildrenObject> {
        return listOf(RedditChildrenObject(
            kind = "search",
            data = RedditChildrenData(
                subreddit,
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
                null,null,
                null,null,
                null,
                null,
                null,
                null,
                null, null, null, null
            )
        ),)
    }

    fun loadDefaultAccountItems(userInfo: UserInfo?): List<RedditChildrenObject> {
        return listOf(
            RedditChildrenObject(
                kind = "userInfo",
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
                    null,null,
                    null,null,
                    null,
                    null,
                    null,
                    null,
                    null, null, null, userInfo
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
                    null,null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,null,
                    null,
                    null,
                    "submitted",
                    null,
                    "Posts", null, null,userInfo
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
                    null,null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,null,
                    null,
                    null,
                    "comments",
                    null,
                    "Comments", null,null,userInfo
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
                    null,null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,null,
                    null,
                    null,
                    null,
                    null,
                    "saved",
                    null,
                    "Saved", null,null,userInfo
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
                    null,null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Friends", null,null,userInfo
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
                    null,null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,null,
                    null,
                    null,
                    "upvoted",
                    null,
                    "Upvoted", null,null,userInfo
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
                    null,null,
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
                    "Downvoted", null,null,userInfo
                )
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(null,null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, "hidden", null, "Hidden", null,null,userInfo
                )
            ),
            RedditChildrenObject(
                kind = "defaultBottom",
                data = RedditChildrenData(null, null,null, null, null,
                    null, null, null, null,
                    null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null,"Trophies", null,null,userInfo
                )
            ),



            )
    }
}