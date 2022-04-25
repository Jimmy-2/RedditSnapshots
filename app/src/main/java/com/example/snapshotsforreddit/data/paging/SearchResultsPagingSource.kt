package com.example.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException

class SearchResultsPagingSource(
    private val redditApiService: RedditApiService,
    private val subredditName: String?,
    private val subredditType: String?,
    private val query: String?,
    private val subredditOnly: Int?,
    private val includeNSFW: String?,
    private val sort: String?,
    ) : PagingSource<String, RedditChildrenObject>() {

    override fun getRefreshKey(state: PagingState<String, RedditChildrenObject>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): PagingSource.LoadResult<String, RedditChildrenObject> {
        return try {

            val responseData = redditApiService.getSearchResults(
                "snapshots-for-reddit",
                limit = params.loadSize,
                after = if (params is LoadParams.Append) params.key else null,
                before = if (params is LoadParams.Prepend) params.key else null,
                type = subredditType ?: "r",
                subreddit = subredditName ?: "aww",
                q = query,
                restrict_sr = subredditOnly ?: 1,
                sr_nsfw = includeNSFW,
                sort = sort ?: "relevance", sr_detail = 1
            ).data


            val searchResults = responseData!!.children



            PagingSource.LoadResult.Page(
                data = searchResults,
                prevKey = responseData.before,
                nextKey = responseData.after
            )

        } catch (exception: IOException) {
            PagingSource.LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }


}