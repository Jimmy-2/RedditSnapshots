package com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jimmywu.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.jimmywu.snapshotsforreddit.network.services.RedditApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RedditPagePostRepository @Inject constructor(
    private val redditApiService: RedditApiService,
    private val redditPagePostDatabase: RedditPagePostDatabase,
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
) {


    private val redditPagePostDao = redditPagePostDatabase.redditPagePostDao()


    fun getRedditPostsPaged(redditPageName: String,
                            sortOrder: String): Flow<PagingData<RedditPagePost>> {
        // appending '%' so we can allow other characters to be before and after the query string

        val pagingSourceFactory = { redditPagePostDao.getRedditPagePosts("$redditPageName $sortOrder")}

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 25, enablePlaceholders = false),
            remoteMediator = RedditPageRemoteMediator(
                preferencesDataStoreRepository,
                redditPagePostDatabase,
                redditApiService,
                redditPageName,
                sortOrder
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
//
//    fun getRedditPostsPaged(redditPageName: String,
//                            sortOrder: String,
//                            isCompact: Boolean): Flow<PagingData<RedditPagePost>> {
//        // appending '%' so we can allow other characters to be before and after the query string
//
//        val pagingSourceFactory = { redditPagePostDao.getRedditPagePosts("$redditPageName $sortOrder")}
//
//        @OptIn(ExperimentalPagingApi::class)
//        return Pager(
//            config = PagingConfig(pageSize = 25, enablePlaceholders = false),
//            remoteMediator = RedditPageRemoteMediator(
//                preferencesDataStoreRepository,
//                redditPagePostDatabase,
//                redditApiService,
//                redditPageName,
//                sortOrder,
//                isCompact
//            ),
//            pagingSourceFactory = pagingSourceFactory
//        ).flow
//    }










    suspend fun updateRedditPagePost(redditPost: RedditPagePost) {
        redditPagePostDao.updateRedditPagePost(redditPost)
    }

    suspend fun getRedditPagePost(postName: String, postRedditPageAndSort: String): RedditPagePost {
        return redditPagePostDao.getRedditPagePost(postName, postRedditPageAndSort).first()
    }

    suspend fun updateCompactView(isCompact: Boolean) {
        redditPagePostDao.updateRedditPageIsCompactLayout(isCompact)
    }


}