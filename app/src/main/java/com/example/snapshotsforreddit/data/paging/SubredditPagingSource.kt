package com.example.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.data.DefaultsDatasource
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException


class SubredditPagingSource(
    private val redditApiService: RedditApiService,
    private val query: String?,
    private val searchType: String?,
    private val includeNSFW: Int?
) : PagingSource<String, SubredditChildrenData>() {

    override fun getRefreshKey(state: PagingState<String, SubredditChildrenData>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, SubredditChildrenData> {

        return try {

            /*
            val response = redditApiService.getSubscribedList("bearer $accessToken","snapshots-for-reddit",
                after = if (params is LoadParams.Append) params.key else null,
            )
             */

            val response = if (query == null) {
                redditApiService.getSubscribedSubreddits(
                    "snapshots-for-reddit",
                    after = if (params is LoadParams.Append) params.key else null,
                    before = if (params is LoadParams.Prepend) params.key else null,
                    limit = 100
                ).data
            } else {
                redditApiService.getSearchResultsSubreddit(
                    "snapshots-for-reddit",
                    query,
                    searchType,
                    includeNSFW,
                    limit = params.loadSize,
                    after = if (params is LoadParams.Append) params.key else null,
                    before = if (params is LoadParams.Prepend) params.key else null,
                ).data
            }


            val subreddits = if (params.key == null && query == null) {
                //first page we will preload with default subreddits (home, popular, and all)
                DefaultsDatasource().loadDefaultSubreddits() + response!!.children.map { it.data }
            } else {
                response!!.children.map { it.data }
            }

            LoadResult.Page(
                data = subreddits,
                prevKey = response.before,
                nextKey = response.after
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }


}