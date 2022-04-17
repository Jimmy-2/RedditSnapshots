package com.example.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.data.DefaultsDatasource
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.network.responses.account.UserData
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException

class AccountOverviewPagingSource(private val redditApiService: RedditApiService, private val username: String, private val userData: UserData?) : PagingSource<String, RedditChildrenObject>(){
    private val TAG: String = "AccountOverviewPagingSource"
    override fun getRefreshKey(state: PagingState<String, RedditChildrenObject>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditChildrenObject> {
        return try {
            val responseData = redditApiService.getUserHistory("snapshots-for-reddit", username = username,
                after = if (params is LoadParams.Append) params.key else null,
                before = if (params is LoadParams.Prepend) params.key else null,
                historyType = "overview"
            ).data

            val overviewItems = if(params.key == null) {
                //DefaultsDatasource().emptyRedditChildrenData(kind = "userData", userData = userData) +
                    if(responseData?.children!!.isEmpty()) {
                        DefaultsDatasource().loadDefaultAccountItems(userData)+ responseData.children
                    }else {
                        DefaultsDatasource().loadDefaultAccountItems(userData)+DefaultsDatasource().addHeader()+ responseData.children
                    }


            }else {
                responseData!!.children
            }

            LoadResult.Page(
                data = overviewItems,
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