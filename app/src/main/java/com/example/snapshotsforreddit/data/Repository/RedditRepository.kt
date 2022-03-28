package com.example.snapshotsforreddit.data.Repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.snapshotsforreddit.data.Paging.SubscribedSubredditsPagingSource
import com.example.snapshotsforreddit.network.services.RedditApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditRepository @Inject constructor(private val redditApi: RedditApi) {
    fun getSubscribedSubredditsResults(accessToken: String?) = Pager(
            PagingConfig(25)) {
                SubscribedSubredditsPagingSource(redditApi, accessToken)
            }.liveData



    companion object {
        const val STARTING_COUNT = 0
    }
}