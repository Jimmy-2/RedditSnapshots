package com.example.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.network.responses.RedditPageChildrenObject
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException

class RedditPagePagingSource(private val redditApiService: RedditApiService, private val accessToken: String?, private val subredditName: String?, private val subredditType: String?) : PagingSource<String, RedditPageChildrenObject>() {
    override fun getRefreshKey(state: PagingState<String, RedditPageChildrenObject>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditPageChildrenObject> {
        return try {
            /*
            val response = redditApiService.getListOfPosts("bearer $accessToken","snapshots-for-reddit", limit = params.loadSize,
                after = if (params is LoadParams.Append) params.key else null,
                before = if (params is LoadParams.Prepend) params.key else null,
                type = subredditType ?: "",
                subreddit = subredditName ?: "",
                sort = "hot"
            )
             */
            val response = redditApiService.getListOfPosts("snapshots-for-reddit", limit = params.loadSize,
                after = if (params is LoadParams.Append) params.key else null,
                before = if (params is LoadParams.Prepend) params.key else null,
                type = subredditType ?: "",
                subreddit = subredditName ?: "",
                sort = "hot"
            )
            //make sort enum

            val posts = response.data!!.children

            LoadResult.Page(
                data = posts,
                prevKey = response.data.before,
                nextKey = response.data.after
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }


}