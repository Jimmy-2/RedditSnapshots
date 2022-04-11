package com.example.snapshotsforreddit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.paging.AccountOverviewPagingSource
import com.example.snapshotsforreddit.data.paging.RedditPagePagingSource
import com.example.snapshotsforreddit.data.paging.SubscribedPagingSource
import com.example.snapshotsforreddit.network.services.RedditApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditApiRepository @Inject constructor(private val redditApiService: RedditApiService) {
    fun getSubscribedResults() = Pager(
        PagingConfig(count)
    ) {
        SubscribedPagingSource(redditApiService)
    }.liveData

    fun getSubredditPostsList(accessToken: String?, subredditName: String, subredditType: String) = Pager(
        PagingConfig(count)
    ) {
        RedditPagePagingSource(redditApiService, accessToken, subredditName, subredditType)
    }.liveData

    fun getUserOverviewList(username: String) = Pager(
        PagingConfig(count)
    ) {
        AccountOverviewPagingSource(redditApiService, username)
    }.liveData




    suspend fun getUsername() = redditApiService.getLoggedInUsername(
         USER_AGENT
    )


    companion object {
        const val count = 25
        const val USER_AGENT = BuildConfig.USER_AGENT
    }
}