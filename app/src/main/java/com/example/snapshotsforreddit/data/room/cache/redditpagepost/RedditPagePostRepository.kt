package com.example.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.room.withTransaction
import com.example.snapshotsforreddit.data.room.cache.subscribedsubreddit.SubscribedSubreddit
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData
import com.example.snapshotsforreddit.network.services.RedditApiService
import com.example.snapshotsforreddit.util.Resource
import com.example.snapshotsforreddit.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RedditPagePostRepository @Inject constructor(
    private val redditApiService: RedditApiService,
    private val redditPagePostDatabase: RedditPagePostDatabase
) {

    private val redditPagePostDao = redditPagePostDatabase.redditPagePostDao()
    fun getRedditPosts(forceRefresh: Boolean): Flow<Resource<List<RedditPagePost>>> =
        networkBoundResource(
            query = {
                redditPagePostDao.getRedditPagePosts()
            },

            fetch = {
                var after: String? = ""
                val subscribedSubredditDataList = mutableListOf<SubredditChildrenData>()

                while (after != null) {
                    //if we try catch the response, we won't get an error and resource file won't return it to us to display to user
//                    try {
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
//                    } catch (e: Exception) {
//                        after = null
//                    }

                }
                subscribedSubredditDataList


            },
            shouldFetch = {
                true
            },
            saveFetchResult = { subscribedSubredditDataList ->
                val subscribedSubredditsList =
                    subscribedSubredditDataList.map { subredditChildrenData ->
                        SubscribedSubreddit(
                            dataKind = subredditChildrenData.dataKind,
                            sortPriority = subredditChildrenData.sortPriority,
                            display_name = subredditChildrenData.display_name,
                            display_name_prefixed = subredditChildrenData.display_name_prefixed,
                            subscribers = subredditChildrenData.subscribers,
                            created_utc = subredditChildrenData.created_utc,
                            community_icon = subredditChildrenData.community_icon,
                            primary_color = subredditChildrenData.primary_color,
                            icon_img = subredditChildrenData.icon_img,
                            subreddit_type = subredditChildrenData.subreddit_type,
                            user_has_favorited = subredditChildrenData.user_has_favorited,
                            public_description = subredditChildrenData.public_description,
                            name = subredditChildrenData.name
                        )
                    } + SubscribedSubreddit(
                        "default",
                        0,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,null,
                        true,
                        null,"default"

                    ) +  SubscribedSubreddit(
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
                        null, "header"
                    )


                subscribedSubredditDatabase.withTransaction {
                    subscribedSubredditDao.deleteSubscribedSubredditsForRefresh()

                    subscribedSubredditDao.insertSubscribedSubreddits(subscribedSubredditsList)
                }

            } ,
//            onFetchFailed = { t ->
//                if (t !is HttpException && t !is IOException) {
//                    throw t
//                }
//                onFetchFailed(t)
//            }


        )
}