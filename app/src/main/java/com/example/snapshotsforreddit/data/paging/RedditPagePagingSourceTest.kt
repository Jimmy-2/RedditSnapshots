package com.example.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.data.DefaultsDatasource
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.responses.RedditData
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException

class RedditPagePagingSourceTest (
    private val redditApiService: RedditApiService,
    private val subredditName: String?,
    private val subredditType: String?,
    private val sort: String?,
    private val isCompact: Boolean?
) : PagingSource<String, RedditChildrenData>() {

    override fun getRefreshKey(state: PagingState<String, RedditChildrenData>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    //asynchronously fetch more posts(data) as the user scrolls
    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditChildrenData> {
        return try {
            val responseData: RedditData? = if (subredditType == "r" && subredditName == "Home") {
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
                    pageType = subredditType ?: "",
                    subreddit = subredditName ?: "",
                    sort = sort ?: "best"
                ).data
            }

            //TODO MAP KIND INTO NEXT VARS


            //TODO MAP KIND INTO NEXT VARS
            val posts = if (params.key == null) {
                DefaultsDatasource().addSearchBarTest(subredditName!!, isCompact?: false) + responseData!!.children.map { it.data }
            } else {
                responseData!!.children.map { it.data }
            }


            LoadResult.Page(
                data = posts,
                prevKey = responseData.before,
                nextKey = responseData.after
                //itemsBefore: The number of placeholders to show before the loaded data.
                //itemsAfter: The number of placeholders to show after the loaded data.
            )


        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }


}