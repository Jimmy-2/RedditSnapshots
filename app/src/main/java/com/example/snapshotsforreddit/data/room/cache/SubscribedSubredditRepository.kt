package com.example.snapshotsforreddit.data.room.cache

import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData
import com.example.snapshotsforreddit.network.services.RedditApiService
import javax.inject.Inject

class SubscribedSubredditRepository @Inject constructor(
    private val redditApiService: RedditApiService,
    private val subscribedSubredditDatabase: SubscribedSubredditDatabase
) {

    private val subscribedSubredditDao = subscribedSubredditDatabase.subscribedSubredditDao()


    suspend fun getSubscribedSubreddits(): List<SubscribedSubreddit> {
        var after: String? = ""
        val subscribedSubredditDataList = mutableListOf<SubredditChildrenData>()

        while (after != null) {
            try {
                val response = redditApiService.getSubscribedSubreddits(
                    "snapshots-for-reddit",
                    after = after,
                    limit = 100
                )
                val data = response.data

                if (data != null) {
                    val mapped = data.children.map { it.data }
                    subscribedSubredditDataList.addAll(mapped)
                    after = data.after
                } else {
                    after = null
                }
            }catch (e: Exception) {
                after = null
            }

        }

        val subscribedSubredditList: List<SubscribedSubreddit> =
            subscribedSubredditDataList.map { subscribedSubredditData ->
                SubscribedSubreddit(
                    dataKind = subscribedSubredditData.dataKind,
                    display_name = subscribedSubredditData.display_name,
                    display_name_prefixed = subscribedSubredditData.display_name_prefixed,
                    subscribers = subscribedSubredditData.subscribers,
                    created_utc = subscribedSubredditData.created_utc,
                    community_icon = subscribedSubredditData.community_icon,
                    primary_color = subscribedSubredditData.primary_color,
                    icon_img = subscribedSubredditData.icon_img,
                    subreddit_type = subscribedSubredditData.subreddit_type,
                    user_has_favorited = subscribedSubredditData.user_has_favorited,
                    public_description = subscribedSubredditData.public_description,
                    name = subscribedSubredditData.name
                )
            }

        return subscribedSubredditList.sortedBy {
            it.display_name
        }
    }
}