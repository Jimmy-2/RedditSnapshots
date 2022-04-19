package com.example.snapshotsforreddit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.paging.AccountOverviewPagingSource
import com.example.snapshotsforreddit.data.paging.RedditPagePagingSource
import com.example.snapshotsforreddit.data.paging.SubscribedPagingSource
import com.example.snapshotsforreddit.network.responses.account.UserData
import com.example.snapshotsforreddit.network.services.RedditApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditApiRepository @Inject constructor(private val redditApiService: RedditApiService) {
    fun getSubscribedSubredditsList() = Pager(
        PagingConfig(count)
    ) {
        SubscribedPagingSource(redditApiService)
    }.liveData

    fun getSubredditPostsList(subredditName: String, subredditType: String) =
        Pager(
            PagingConfig(count)
        ) {
            RedditPagePagingSource(redditApiService, subredditName, subredditType)
        }.liveData


    fun getUserOverviewList(username: String?, userData: UserData?) = Pager(
        PagingConfig(count)
    ) {
        AccountOverviewPagingSource(redditApiService, username?: "", userData)
    }.liveData


    suspend fun getUserInfoData(username: String) =
        redditApiService.getUserInfo(USER_AGENT, username)

    suspend fun getUsername() = redditApiService.getLoggedInUsername(
        USER_AGENT
    )

    suspend fun voteOnThing(dir: Int, id: String) = redditApiService.vote(USER_AGENT,dir, id)


    companion object {
        const val count = 25
        const val USER_AGENT = BuildConfig.USER_AGENT
    }
}