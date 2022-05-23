package com.example.snapshotsforreddit.data

import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.network.responses.Defaults
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
                data = SubscribedChildrenData(
                    "Home",
                    "Home",
                    R.drawable.ic_home,
                    null,
                    null,
                    "#f80e5f",
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
                    R.drawable.ic_popular,
                    null,
                    null,
                    "#0091ff",
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
                    R.drawable.ic_all,
                    null,
                    null,
                    "#0cd23c",
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
            RedditChildrenObject(kind = "header", data = RedditChildrenData(), defaults = null)
        )
    }

    fun addSearchBar(subreddit: String, isCompact: Boolean): List<RedditChildrenObject> {
        return listOf(
            RedditChildrenObject(
                kind = "search",
                data = RedditChildrenData(),
                defaults = Defaults(subreddit, null, null, null, isCompact)
            ),
        )
    }
    fun addSearchBarTest(subreddit: String, isCompact: Boolean): List<RedditChildrenData> {
        return listOf(
            RedditChildrenData(
                dataKind = "search",
                subreddit = subreddit,
                isCompact = isCompact
            ),
        )
    }

    fun loadDefaultUserItems(userInfo: UserInfo?, isCompact: Boolean): List<RedditChildrenObject> {
        return listOf(
            RedditChildrenObject(
                kind = "userInfo",
                data = RedditChildrenData(),
                defaults = Defaults(null, null, userInfo, null, isCompact)
            ),
            RedditChildrenObject(
                kind = "defaultTop",
                data = RedditChildrenData(),
                defaults = Defaults("submitted", "Posts", userInfo, R.drawable.ic_post_temp)
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(),
                defaults = Defaults("comments", "Comments", userInfo, R.drawable.ic_comment)
            ),
//            RedditChildrenObject(
//                kind = "default",
//                data = com.example.snapshotsforreddit.network.responses.RedditChildrenData(),
//                defaults = Defaults(null, "Multireddits", userInfo)
//            ),
            RedditChildrenObject(
                kind = "defaultBottom",
                data = RedditChildrenData(),
                defaults = Defaults(null, "Trophies", userInfo, R.drawable.ic_trophies)
            ),
        )
    }


    fun loadDefaultAccountItems(userInfo: UserInfo?, isCompact: Boolean): List<RedditChildrenObject> {
        return listOf(
            RedditChildrenObject(
                kind = "userInfo",
                data = RedditChildrenData(),
                defaults = Defaults(null, null, userInfo,null, isCompact)
            ),
            RedditChildrenObject(
                kind = "defaultTop",
                data = RedditChildrenData(),
                defaults = Defaults("submitted", "Posts", userInfo, R.drawable.ic_post_temp)
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(),
                defaults = Defaults("comments", "Comments", userInfo,R.drawable.ic_comment)
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(),
                defaults = Defaults("saved", "Saved", userInfo,R.drawable.ic_save)
            ),
//            RedditChildrenObject(
//                kind = "default",
//                data = com.example.snapshotsforreddit.network.responses.RedditChildrenData(),
//                defaults = Defaults(null, "Friends", userInfo)
//            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(),
                defaults = Defaults("upvoted", "Upvoted", userInfo,R.drawable.ic_up_arrow_null)
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(),
                defaults = Defaults("downvoted", "Downvoted", userInfo,R.drawable.ic_down_arrow_null)
            ),
            RedditChildrenObject(
                kind = "default",
                data = RedditChildrenData(),
                defaults = Defaults("hidden", "Hidden", userInfo,R.drawable.ic_hidden)
            ),
            RedditChildrenObject(
                kind = "defaultBottom",
                data = RedditChildrenData(),
                defaults = Defaults(null, "Trophies", userInfo,R.drawable.ic_trophies)
            )
        )
    }
}