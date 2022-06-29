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

    fun getRedditPostsPaged(redditPageName: String,
                            sortOrder: String,
                            isCompact: Boolean): Flow<PagingData<RedditPagePost>> {
        // appending '%' so we can allow other characters to be before and after the query string

        val pagingSourceFactory = { redditPagePostDao.getRedditPagePosts("$redditPageName $sortOrder")}

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 25, enablePlaceholders = false),
            remoteMediator = RedditPageRemoteMediator(
                redditPagePostDatabase,
                redditApiService,
                redditPageName,
                sortOrder,
                isCompact
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    suspend fun updateRedditPagePage(redditPost: RedditPagePost) {
        redditPagePostDao.updateRedditPagePost(redditPost)
    }
}