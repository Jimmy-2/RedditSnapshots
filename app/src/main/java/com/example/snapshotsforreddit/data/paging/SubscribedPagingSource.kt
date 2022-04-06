package com.example.snapshotsforreddit.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.data.DefaultsDatasource
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException




class SubscribedPagingSource(private val redditApiService: RedditApiService, private val accessToken: String?) : PagingSource<String, SubscribedChildrenObject>() {
    private val TAG: String = "SubscribedPagingSource"

    override fun getRefreshKey(state: PagingState<String, SubscribedChildrenObject>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, SubscribedChildrenObject> {

        return try {

            /*
            val response = redditApiService.getSubscribedList("bearer $accessToken","snapshots-for-reddit",
                after = if (params is LoadParams.Append) params.key else null,
            )
             */
            val response = redditApiService.getSubscribedList("snapshots-for-reddit",
                after = if (params is LoadParams.Append) params.key else null,
            )

            val subreddits = if(params.key == null) {
                //first page we will preload with default subreddits (home, popular, and all)
                DefaultsDatasource().loadDefaultSubreddits()+response.data!!.children
            }else {
                response.data!!.children
            }

            LoadResult.Page(
                data = subreddits,
                prevKey = response.data.before,
                nextKey = response.data.after
            )
        } catch (exception: IOException) {
            Log.v(TAG, "HELLO $exception")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.v(TAG, "HELLO $exception")
            LoadResult.Error(exception)
        }
    }


}