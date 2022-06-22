package com.example.snapshotsforreddit.data

import com.example.snapshotsforreddit.data.room.cache.subscribedsubreddit.SubscribedSubreddit
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData

class DefaultsDatasource {

    val subscribedSubredditDefaults =
        SubscribedSubreddit(
            "default",
            0,
            null,
            null,
            null,
            null,
            null,
            null,
            null, null,
            true,
            null, ""

        )

    val subscribedSubredditHeader =
        SubscribedSubreddit(
            "header",
            1,
            null,
            null,
            null,
            null,
            null,
            null,
            null, null,
            true,
            null, ""
        )


    fun loadDefaultSubreddits(): List<SubredditChildrenData> {
        return listOf(
            SubredditChildrenData(
                "default",
                2,
                null,
                null,
                null,
                null,
                null,
                null,
                null, null,
                true,
                null, ""

            ),


            SubredditChildrenData(
                "header",
                2,
                null,

                null,
                null,
                null,
                null,
                null,
                null, null,
                true,
                null, ""
            ),
        )
    }


    fun addHeader(): List<RedditChildrenData> {
        return listOf(
            RedditChildrenData(dataKind = "header")
        )
    }

    fun addSearchBar(
        subredditName: String,
        isDefault: Boolean,
        isCompact: Boolean
    ): List<RedditChildrenData> {
        return listOf(
            RedditChildrenData(
                dataKind = "search",
                subreddit = subredditName,
                isDefault = isDefault,
                isCompact = isCompact
            ),
        )
    }

    fun loadDefaultUserItems(userInfo: UserInfo?, isCompact: Boolean): List<RedditChildrenData> {
        return listOf(
            RedditChildrenData(
                dataKind = "userInfo",
                isCompact = isCompact,
                user_name = userInfo?.name,
                link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma,
                awarder_karma = userInfo?.awarder_karma,
                awardee_karma = userInfo?.awardee_karma,
                user_created_utc = userInfo?.created_utc
            ),

            RedditChildrenData(dataKind = "history", user_name = userInfo?.name)

        )
    }


    fun loadDefaultAccountItems(userInfo: UserInfo?, isCompact: Boolean): List<RedditChildrenData> {
        return listOf(
            RedditChildrenData(
                dataKind = "userInfo",
                isCompact = isCompact,
                user_name = userInfo?.name,
                link_karma = userInfo?.link_karma,
                comment_karma = userInfo?.comment_karma,
                total_karma = userInfo?.total_karma,
                awarder_karma = userInfo?.awarder_karma,
                awardee_karma = userInfo?.awardee_karma,
                user_created_utc = userInfo?.created_utc
            ),


            RedditChildrenData(dataKind = "history", user_name = userInfo?.name)


        )
    }
}