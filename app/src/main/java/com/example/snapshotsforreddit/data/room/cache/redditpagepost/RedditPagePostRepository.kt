package com.example.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.snapshotsforreddit.network.services.RedditApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RedditPagePostRepository @Inject constructor(
    private val redditApiService: RedditApiService,
    private val redditPagePostDatabase: RedditPagePostDatabase
) {

    private val redditPagePostDao = redditPagePostDatabase.redditPagePostDao()

    @OptIn(ExperimentalPagingApi::class)
    fun getRedditPostsPaged(redditPageName: String, sortOrder: String): Flow<PagingData<RedditPagePost>> =
        Pager(

            config = PagingConfig(30),
            remoteMediator = RedditPageRemoteMediator(redditPagePostDatabase, redditApiService, redditPageName, sortOrder),
        ){
            println("HELLO WORLD")
            redditPagePostDao.getRedditPagePosts(redditPageName, sortOrder)
        }.flow





    suspend fun updateRedditPagePage(redditPost: RedditPagePost) {
        redditPagePostDao.updateRedditPagePost(redditPost)
    }
}