package com.example.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException

class RedditPagePagingSource(
    private val redditApiService: RedditApiService,
    private val subredditName: String?,
    private val subredditType: String?
) : PagingSource<String, RedditChildrenObject>() {
    override fun getRefreshKey(state: PagingState<String, RedditChildrenObject>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditChildrenObject> {
        return try {

            val response = if (subredditType == "" && subredditName == "") {
                redditApiService.getListOfHomePosts(
                    "snapshots-for-reddit", limit = params.loadSize,
                    after = if (params is LoadParams.Append) params.key else null,
                    before = if (params is LoadParams.Prepend) params.key else null, sort = "best"
                )
            } else {
                redditApiService.getListOfPosts(
                    "snapshots-for-reddit", limit = params.loadSize,
                    after = if (params is LoadParams.Append) params.key else null,
                    before = if (params is LoadParams.Prepend) params.key else null,
                    type = subredditType ?: "",
                    subreddit = subredditName ?: "",
                    sort = "best"
                )
            }


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