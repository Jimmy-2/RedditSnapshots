package com.example.snapshotsforreddit.data.Paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.network.responses.ChildrenObject
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject
import com.example.snapshotsforreddit.network.services.RedditApi
import retrofit2.HttpException
import java.io.IOException




class SubscribedSubredditsPagingSource(private val redditApi: RedditApi,private val accessToken: String?) : PagingSource<String, SubscribedChildrenObject>() {
    override fun getRefreshKey(state: PagingState<String, SubscribedChildrenObject>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, SubscribedChildrenObject> {
        println("HEY ACCESS $accessToken")
        return try {

            val response = redditApi.getSubscribedList("bearer $accessToken","snapshots-for-reddit",
                after = if (params is LoadParams.Append) params.key else null,
                before = if (params is LoadParams.Prepend) params.key else null,
            )

            val subreddits = response.data!!.children
            LoadResult.Page(
                data = subreddits,
                prevKey = response.data.before,
                nextKey = response.data.after
            )
        } catch (exception: IOException) {
            println("HELLO5")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            println("HELLO6")
            LoadResult.Error(exception)
        }
    }


}