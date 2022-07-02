package com.jimmywu.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jimmywu.snapshotsforreddit.network.responses.RedditChildrenData
import com.jimmywu.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException

class SearchResultsPagingSource(
    private val redditApiService: RedditApiService,
    private val subredditName: String?,
    private val subredditType: String?,
    private val query: String?,
    private val searchType: String?,
    private val includeNSFW: String?,
    private val sort: String?,
    private val isCompact: Boolean?,
    ) : PagingSource<String, RedditChildrenData>() {

    override fun getRefreshKey(state: PagingState<String, RedditChildrenData>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditChildrenData> {
        return try {
            val responseData =
                redditApiService.getSearchResults(
                    "snapshots-for-reddit",
                    pageType = subredditType ?: "r",
                    subreddit = subredditName ?: "all",
                    q = query,
                    restrict_sr = if(subredditName == null) 0 else 1,
                    sr_nsfw = includeNSFW,
                    type = searchType,
                    limit = params.loadSize,
                    after = if (params is LoadParams.Append) params.key else null,
                    before = if (params is LoadParams.Prepend) params.key else null,
                    sort = sort ?: "relevance", sr_detail = 1
                ).data

            val searchResults = responseData!!.children.map{it.data}
            
            LoadResult.Page(
                data = searchResults,
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