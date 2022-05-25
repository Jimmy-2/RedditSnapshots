package com.example.snapshotsforreddit.data

import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData

class DefaultsDatasource {

    fun loadDefaultSubreddits(): List<SubredditChildrenData> {
        return listOf(
            SubredditChildrenData(
                    "default",
                    "Home",
                    "Home",
                    R.drawable.ic_home,
                    null,
                    null,
                    "#f80e5f",
                    null,
                    true,
                    "Posts from your subscribed subreddits",
                null

            ),
            SubredditChildrenData(

                    "default",
                    "Popular Posts",
                    "r/Popular",
                    R.drawable.ic_popular,
                    null,
                    null,
                    "#0091ff",
                    "r",
                    true,
                    "Popular recommended posts from Reddit",null

            ),
            SubredditChildrenData(
                    "default",
                    "All Posts",
                    "r/All",
                    R.drawable.ic_all,
                    null,
                    null,
                    "#0cd23c",
                    "r",
                    true,
                    "Most active posts from all of Reddit",null
                ),


            SubredditChildrenData(
                "header",
                "All Posts",
                "r/All",
                R.drawable.ic_all,
                null,
                null,
                "#0cd23c",
                "r",
                true,
                "Most active posts from all of Reddit",null
            ),
        )
    }

    fun addHeader(): List<RedditChildrenData> {
        return listOf(
            RedditChildrenData(dataKind = "header")
        )
    }

    fun addSearchBar(subreddit: String, isCompact: Boolean): List<RedditChildrenData> {
        return listOf(
            RedditChildrenData(
                dataKind = "search",
                subreddit = subreddit,
                isCompact = isCompact
            ),
        )
    }

    fun loadDefaultUserItems(userInfo: UserInfo?, isCompact: Boolean): List<RedditChildrenData> {
        return listOf(
            RedditChildrenData(dataKind = "userInfo", isCompact = isCompact,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma, total_karma = userInfo?.total_karma, awarder_karma = userInfo?.awarder_karma,
                awardee_karma = userInfo?.awardee_karma, user_created_utc = userInfo?.created_utc),


            RedditChildrenData(dataKind = "defaultTop", history_type = "posts", history_name = "Posts", icon = R.drawable.ic_post_temp,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),

            RedditChildrenData(dataKind = "default", history_type = "comments", history_name = "Comments", icon = R.drawable.ic_comment,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),

            RedditChildrenData(dataKind = "defaultBottom", history_type = "null", history_name = "Trophies", icon = R.drawable.ic_trophies,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),


        )
    }


    fun loadDefaultAccountItems(userInfo: UserInfo?, isCompact: Boolean): List<RedditChildrenData> {
        return listOf(
            RedditChildrenData(dataKind = "userInfo", isCompact = isCompact,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma, total_karma = userInfo?.total_karma, awarder_karma = userInfo?.awarder_karma,
                awardee_karma = userInfo?.awardee_karma, user_created_utc = userInfo?.created_utc),


            RedditChildrenData(dataKind = "defaultTop", history_type = "posts", history_name = "Posts", icon = R.drawable.ic_post_temp,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),

            RedditChildrenData(dataKind = "default", history_type = "comments", history_name = "Comments", icon = R.drawable.ic_comment,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),

            RedditChildrenData(dataKind = "default", history_type = "saved", history_name = "Saved", icon = R.drawable.ic_save,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),

            RedditChildrenData(dataKind = "default", history_type = "upvoted", history_name = "Upvoted", icon = R.drawable.ic_up_arrow_null,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),

            RedditChildrenData(dataKind = "default", history_type = "downvoted", history_name = "Downvoted", icon = R.drawable.ic_down_arrow_null,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),
            RedditChildrenData(dataKind = "default", history_type = "hidden", history_name = "Hidden", icon = R.drawable.ic_hidden,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),
            RedditChildrenData(dataKind = "defaultBottom", history_type = "null", history_name = "Trophies", icon = R.drawable.ic_trophies,
                user_name = userInfo?.name, link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma, user_created_utc = userInfo?.created_utc),
        )
    }
}