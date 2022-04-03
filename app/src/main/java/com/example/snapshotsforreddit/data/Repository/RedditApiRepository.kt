package com.example.snapshotsforreddit.data.Repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.Paging.RedditPagePagingSource
import com.example.snapshotsforreddit.data.Paging.SubscribedPagingSource
import com.example.snapshotsforreddit.network.services.RedditApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditApiRepository @Inject constructor(private val redditApiService: RedditApiService) {
    fun getSubscribedResults(accessToken: String?) = Pager(
        PagingConfig(count)
    ) {
        SubscribedPagingSource(redditApiService, accessToken)
    }.liveData

    fun getSubredditPostsList(accessToken: String?, subredditName: String, subredditType: String) = Pager(
        PagingConfig(count)
    ) {
        RedditPagePagingSource(redditApiService, accessToken, subredditName, subredditType)
    }.liveData

    suspend fun getUsername(accessToken: String?) = redditApiService.getLoggedInUsername(
        "bearer $accessToken", USER_AGENT
    )


    companion object {
        const val count = 25
        const val USER_AGENT = BuildConfig.USER_AGENT
    }
}