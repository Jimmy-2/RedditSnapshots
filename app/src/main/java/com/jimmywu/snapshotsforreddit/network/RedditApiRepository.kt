package com.jimmywu.snapshotsforreddit.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.jimmywu.snapshotsforreddit.BuildConfig
import com.jimmywu.snapshotsforreddit.data.paging.*
import com.jimmywu.snapshotsforreddit.network.services.RedditApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditApiRepository @Inject constructor(
    private val redditApiService: RedditApiService,
//    private val redditChildrenObjectDatabase: RedditChildrenObjectDatabase
) {
//    private val redditChildrenObjectDao = redditChildrenObjectDatabase.redditChildrenObjectDao()
    //TODO CHANGE PAGING SIZE AFTER ADDING CACHING TO PAGINATION

    fun getSubscribedSubredditsList() = Pager(
        PagingConfig(50)
    ) {
        SubredditPagingSource(redditApiService, null, null, null)
    }.liveData


//    fun getRedditPagePosts() = networkBoundResource(
//        query = {
//            redditChildrenObjectDao.getRedditChildrenObjects()
//        },
//        fetch = {
//            delay(2000)
//            redditApiService.getRestaurants()
//        },
//        saveFetchResult = { restaurants ->
//            db.withTransaction {
//                restaurantDao.deleteAllRestaurants()
//                restaurantDao.insertRestaurants(restaurants)
//            }
//        }
//    )


    fun getSubredditPostsList(
        subredditName: String,
        subredditType: String,
        isDefault: Boolean?,
        sort: String?,
        isCompact: Boolean?
    ) =
        Pager(
            PagingConfig(count)
        ) {
            RedditPagePagingSource(redditApiService, subredditName, subredditType, isDefault, sort, isCompact)
        }.liveData

    fun getInboxList(inboxType: String) = Pager(
    PagingConfig(count)
    ) {
        InboxPagingSource(redditApiService, inboxType)
    }.liveData


    fun getSearchResultsList(
        subredditName: String?,
        subredditType: String,
        query: String?,
        searchType: String?,
        includeNSFW: String?,
        sort: String?,
        isCompact: Boolean?
    ) =
        Pager(
            PagingConfig(count)
        ) {
            SearchResultsPagingSource(
                redditApiService,
                subredditName,
                subredditType,
                query,
                searchType,
                includeNSFW,
                sort,
                isCompact
            )
        }.liveData

    fun getSearchResultsSubreddit(query: String?, searchType: String?, includeNSFW: Int?) =
        Pager(
            PagingConfig(50)
        ) {
            SubredditPagingSource(redditApiService, query, searchType, includeNSFW)
        }.liveData


//    fun getUserOverviewList(
//        username: String?,
//        userInfo: UserInfo?,
//        accountType: Int,
//        isCompact: Boolean?
//    ) = Pager(
//        PagingConfig(count)
//    ) {
//        OverviewPagingSource(
//            redditApiService,
//            username ?: "",
//            userInfo,
//            "overview",
//            accountType,
//            isCompact
//        )
//    }.liveData

    fun getUserOverviewList(
        username: String?,
        accountType: Int,
        isCompact: Boolean?
    ) = Pager(
        PagingConfig(count)
    ) {
        OverviewPagingSource(
            redditApiService,
            username ?: "",
            "overview",
            accountType,
            isCompact
        )
    }.liveData


    fun getUserPostsList(
        username: String?,
        historyType: String,
        isCompact: Boolean?
    ) = Pager(
        PagingConfig(count)
    ) {
        OverviewPagingSource(
            redditApiService,
            username ?: "",
            historyType,
            null,
            isCompact
        )
    }.liveData

    suspend fun getSubscribedSubredditsTest(after: String?) = redditApiService.getSubscribedSubreddits("snapshots-for-reddit",after = after, limit = 100)

    suspend fun getUserInfoData(username: String) =
        redditApiService.getUserInfo(USER_AGENT, username)

    suspend fun getUsername() = redditApiService.getLoggedInUsername(
        USER_AGENT
    )

    suspend fun voteOnThing(dir: Int, id: String) = redditApiService.vote(USER_AGENT, dir, id)


    companion object {
        const val count = 25
        const val USER_AGENT = BuildConfig.USER_AGENT
    }
}