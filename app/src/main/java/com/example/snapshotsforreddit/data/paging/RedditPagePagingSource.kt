package com.example.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.data.DefaultsDatasource
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException

class RedditPagePagingSource(
    private val redditApiService: RedditApiService,
    private val subredditName: String?,
    private val subredditType: String?,
    private val sort: String?,
) : PagingSource<String, RedditChildrenObject>() {

    override fun getRefreshKey(state: PagingState<String, RedditChildrenObject>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): PagingSource.LoadResult<String, RedditChildrenObject> {
        return try {
            val responseData = if (subredditType == "" && subredditName == "") {
                redditApiService.getHomePosts(
                    "snapshots-for-reddit",
                    limit = params.loadSize,
                    after = if (params is LoadParams.Append) params.key else null,
                    before = if (params is LoadParams.Prepend) params.key else null,
                    sort = sort ?: "best"
                ).data
            } else {
                redditApiService.getSubredditPosts(
                    "snapshots-for-reddit", limit = params.loadSize,
                    after = if (params is LoadParams.Append) params.key else null,
                    before = if (params is LoadParams.Prepend) params.key else null,
                    type = subredditType ?: "",
                    subreddit = subredditName ?: "",
                    sort = sort ?: "best"
                ).data
            }

            val posts = if (params.key == null) {
                DefaultsDatasource().addSearchBar(subredditName!!) + responseData!!.children
            } else {
                responseData!!.children
            }


            PagingSource.LoadResult.Page(
                data = posts,
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