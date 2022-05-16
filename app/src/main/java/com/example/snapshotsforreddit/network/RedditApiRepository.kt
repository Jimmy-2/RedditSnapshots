package com.example.snapshotsforreddit.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.paging.*
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import com.example.snapshotsforreddit.network.services.RedditApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditApiRepository @Inject constructor(private val redditApiService: RedditApiService) {

    //TODO CHANGE PAGING SIZE AFTER ADDING CACHING TO PAGINATION

    fun getSubscribedSubredditsList() = Pager(
        PagingConfig(50)
    ) {
        SubredditPagingSource(redditApiService, null, null, null)
    }.liveData

    fun getSubredditPostsList(
        subredditName: String,
        subredditType: String,
        sort: String?,
        isCompact: Boolean?
    ) =
        Pager(
            PagingConfig(count)
        ) {
            RedditPagePagingSource(redditApiService, subredditName, subredditType, sort, isCompact)
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

    fun getSearchResultsSubredditList(query: String?, searchType: String?, includeNSFW: Int?) =
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