package com.example.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException

class InboxPagingSource(private val redditApiService: RedditApiService, private val inboxType: String,): PagingSource<String, RedditChildrenData>() {
    override fun getRefreshKey(state: PagingState<String, RedditChildrenData>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditChildrenData> {
        return try {
            val responseData = redditApiService.getInbox("snapshots-for-reddit", inboxType,
                after = if (params is LoadParams.Append) params.key else null,
                before = if (params is LoadParams.Prepend) params.key else null).data


            val inboxItems = responseData!!.children.map { it.data }

            LoadResult.Page(
                data = inboxItems,
                prevKey = responseData.before,
                nextKey = responseData.after
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}