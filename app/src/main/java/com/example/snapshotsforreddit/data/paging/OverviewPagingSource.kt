package com.example.snapshotsforreddit.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.snapshotsforreddit.data.DefaultsDatasource
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException

class OverviewPagingSource(
    private val redditApiService: RedditApiService,
    private val username: String,
    private val historyType: String,
    private val accountType: Int?,
    private val isCompact: Boolean?
) : PagingSource<String, RedditChildrenData>() {


    override fun getRefreshKey(state: PagingState<String, RedditChildrenData>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditChildrenData> {
        return try {
            val userInfo =
                redditApiService.getUserInfo(RedditApiRepository.USER_AGENT, username).data

            val responseData = redditApiService.getUserHistory(
                        "snapshots-for-reddit", username = username,
                        after = if (params is LoadParams.Append) params.key else null,
                        before = if (params is LoadParams.Prepend) params.key else null,
                        historyType = historyType
                    ).data



            val overviewItems = if (params.key == null && historyType == "overview") {
                //DefaultsDatasource().emptyRedditChildrenData(kind = "userData", userData = userData) +
                if (responseData?.children!!.isEmpty()) {
                    when (accountType) {
                        0 -> DefaultsDatasource().loadDefaultAccountItems(userInfo, isCompact ?: false) + responseData.children.map { it.data }

                        else -> DefaultsDatasource().loadDefaultUserItems(userInfo, isCompact ?: false) + responseData.children.map { it.data }
                    }

                } else {
                    when (accountType) {
                        0 -> DefaultsDatasource().loadDefaultAccountItems(userInfo, isCompact ?: false) + DefaultsDatasource().addHeader() + responseData.children.map { it.data }

                        else -> DefaultsDatasource().loadDefaultUserItems(userInfo, isCompact ?: false) + DefaultsDatasource().addHeader() + responseData.children.map { it.data }
                    }
                }


            } else {
                responseData!!.children.map { it.data }
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