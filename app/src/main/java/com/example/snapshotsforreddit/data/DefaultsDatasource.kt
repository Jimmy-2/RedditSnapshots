package com.example.snapshotsforreddit.data

import com.example.snapshotsforreddit.network.responses.Defaults
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenObject
import com.example.snapshotsforreddit.network.responses.subreddit.SubscribedChildrenData

class DefaultsDatasource {
    fun loadDefaultSubreddits(): List<SubredditChildrenObject> {
        return listOf(
            SubredditChildrenObject(
                kind = "default",
                data = SubscribedChildrenData(
                    "Home",
                    "Home",
                    null,
                    null,
                    null,
                    null,
                    null,
                    true,
                    "Posts from your subscribed subreddits"
                )
            ),
            SubredditChildrenObject(
                kind = "default",
                data = SubscribedChildrenData(
                    "Popular Posts",
                    "r/Popular",
                    null,
                    null,
                    null,
                    null,
                    "r",
                    true,
                    "Popular recommended posts from Reddit"
                )
            ),
            SubredditChildrenObject(
                kind = "default",
                data = SubscribedChildrenData(
                    "All Posts",
                    "r/All",
                    null,
                    null,
                    null,
                    null,
                    "r",
                    true,
                    "Most active posts from all of Reddit"
                )
            ),

            SubredditChildrenObject(
                kind = "header",
                data = null
            ),
        )
    }

    fun addHeader(): List<RedditChildrenObject> {
        return listOf(
            RedditChildrenObject(kind = "header", data = null, defaults = null)
        )
    }

    fun addSearchBar(subreddit: String, isCompact: Boolean): List<RedditChildrenObject> {
        return listOf(
            RedditChildrenObject(
                kind = "search",
                data = null,
                defaults = Defaults(subreddit, null, null, isCompact)
            ),
        )
    }

    fun loadDefaultUserItems(userInfo: UserInfo?): List<RedditChildrenObject> {
        return listOf(
            RedditChildrenObject(
                kind = "userInfo",
                data = null,
                defaults = Defaults(null, null, userInfo)
            ),
            RedditChildrenObject(
                kind = "defaultTop",
                data = null,
                defaults = Defaults("submitted", "Posts", null)
            ),
            RedditChildrenObject(
                kind = "default",
                data = null,
                defaults = Defaults("comments", "Comments", userInfo)
            ),
            RedditChildrenObject(
                kind = "default",
                data = null,
                defaults = Defaults(null, "Multireddits", userInfo)
            ),
            RedditChildrenObject(
                kind = "defaultBottom",
                data = null,
                defaults = Defaults(null, "Trophies", userInfo)
            ),
        )
    }


    fun loadDefaultAccountItems(userInfo: UserInfo?): List<RedditChildrenObject> {
        return listOf(
            RedditChildrenObject(
                kind = "userInfo",
                data = null,
                defaults = Defaults(null, null, userInfo)
            ),
            RedditChildrenObject(
                kind = "defaultTop",
                data = null,
                defaults = Defaults("submitted", "Posts", userInfo)
            ),
            RedditChildrenObject(
                kind = "default",
                data = null,
                defaults = Defaults("comments", "Comments", userInfo)
            ),
            RedditChildrenObject(
                kind = "default",
                data = null,
                defaults = Defaults("saved", "Saved", userInfo)
            ),
            RedditChildrenObject(
                kind = "default",
                data = null,
                defaults = Defaults(null, "Friends", userInfo)
            ),
            RedditChildrenObject(
                kind = "default",
                data = null,
                defaults = Defaults("upvoted", "Upvoted", userInfo)
            ),
            RedditChildrenObject(
                kind = "default",
                data = null,
                defaults = Defaults("downvoted", "Downvoted", userInfo)
            ),
            RedditChildrenObject(
                kind = "default",
                data = null,
                defaults = Defaults("hidden", "Hidden", userInfo)
            ),
            RedditChildrenObject(
                kind = "defaultBottom",
                data = null,
                defaults = Defaults(null, "Trophies", userInfo)
            )
        )
    }
}