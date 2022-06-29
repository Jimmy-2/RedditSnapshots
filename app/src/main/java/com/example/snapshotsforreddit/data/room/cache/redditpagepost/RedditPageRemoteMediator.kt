package com.example.snapshotsforreddit.data.room.cache.redditpagepost

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.snapshotsforreddit.network.services.RedditApiService
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class RedditPageRemoteMediator (
    private val redditPagePostDatabase: RedditPagePostDatabase,
    private val redditApiService: RedditApiService,
    private val redditPageName: String,
    private val redditPageSortOrder: String,
    private val redditPageIsCompact: Boolean,
) : RemoteMediator<Int, RedditPagePost>() {

    private val redditPagePostDao: RedditPagePostDao = redditPagePostDatabase.redditPagePostDao()
    private val redditPageNameRemoteKeyDao: RedditPageNameRemoteKeyDao = redditPagePostDatabase.redditPageNameRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, RedditPagePost>
//    ): MediatorResult {
//
//        val loadKey = when (loadType) {
//            LoadType.REFRESH -> null
//            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
//            LoadType.APPEND -> redditPageNameRemoteKeyDao.getRemoteKeys("$redditPageName $redditPageSortOrder").nextPageKey
//        }
//
//
//        try {
//
//
//            val responseData = if(redditPageName == "Home Feed") {
//                redditApiService.getHomePosts(
//                    User_Agent = "snapshots-for-reddit",
//                    sort = redditPageSortOrder,
//                    limit = when (loadType) {
//                        LoadType.REFRESH -> state.config.initialLoadSize
//                        else -> state.config.pageSize
//                    },
//                    after = loadKey,
//                    before = null,
//                ).data
//            }else {
//                redditApiService.getSubredditPosts(
//                    User_Agent = "snapshots-for-reddit",
//                    pageType = "r",
//                    subreddit = redditPageName,
//                    sort = redditPageSortOrder,
//                    limit = when (loadType) {
//                        LoadType.REFRESH -> state.config.initialLoadSize
//                        else -> state.config.pageSize
//                    },
//                    after = loadKey,
//                    before = null,
//
//                    ).data
//            }
//
//            val redditChildrenDataList = responseData!!.children.map { it.data }
//
//            val redditPagePostsList = redditChildrenDataList.map { redditChildrenData ->
//                    RedditPagePost(
//                        redditPageSortOrder = redditPageSortOrder,
//
//                        isCompact = redditPageIsCompact,
//
//                        redditPageNameAndSortOrder = "$redditPageName $redditPageSortOrder",
//                        redditPageName = redditPageName,
//                        name = redditChildrenData.name,
//                        subreddit = redditChildrenData.subreddit,
//
//                        selftext = redditChildrenData.selftext,
//
//                        saved = redditChildrenData.saved,
//
//
//                        likes = redditChildrenData.likes,
//
//                        title = redditChildrenData.title,
//
//                        subreddit_name_prefixed = redditChildrenData.subreddit_name_prefixed,
//
//                        is_reddit_media_domain = redditChildrenData.is_reddit_media_domain,
//
//                        score = redditChildrenData.score,
//
//
//                        thumbnail = redditChildrenData.thumbnail,
//
//
//                        is_self = redditChildrenData.is_self,
//
//
//                        url_overridden_by_dest = redditChildrenData.url_overridden_by_dest,
//
//
//                        archived = redditChildrenData.archived,
//
//                        no_follow = redditChildrenData.no_follow,
//
//                        is_crosspostable = redditChildrenData.is_crosspostable,
//
//                        over_18 = redditChildrenData.over_18,
//
//
//
//                        id = redditChildrenData.id,
//
//
//                        author = redditChildrenData.author,
//
//
//                        num_comments = redditChildrenData.num_comments,
//
//
//
//
//                        //not used atm
//                        permalink = redditChildrenData.permalink,
//
//
//                        is_video = redditChildrenData.is_video,
//
//                        body = redditChildrenData.body,
//
//                        link_title = redditChildrenData.link_title,
//
//
//                        created_utc = redditChildrenData.created_utc,
//
//
//                        //mapped content
//
//                        previewUrl = redditChildrenData.preview?.images?.get(0)?.source?.url,
//                        previewWidth = redditChildrenData.preview?.images?.get(0)?.source?.width,
//                        previewHeight = redditChildrenData.preview?.images?.get(0)?.source?.height,
//
//
//
////                        media: RedditPageMedia
//                        sr_community_icon = redditChildrenData.sr_detail?.community_icon,
//                        sr_icon_img = redditChildrenData.sr_detail?.icon_img,
//
//
//                        //DEFAULTS
//                        history_type = redditChildrenData.history_type,
//                        history_name = redditChildrenData.history_name,
//                        icon = redditChildrenData.icon,
//
//                        //userinfo
//                        user_name = redditChildrenData.user_name,
//                        link_karma = redditChildrenData.link_karma,
//                        comment_karma =redditChildrenData.comment_karma,
//                        total_karma = redditChildrenData.total_karma,
//                        awarder_karma = redditChildrenData.awarder_karma,
//                        awardee_karma = redditChildrenData.awardee_karma,
//                        user_created_utc = redditChildrenData.user_created_utc,
//
//                    )
//
//            }
//            println("HELLO ${redditPageName}")
//            redditPagePostDatabase.withTransaction {
//                if (loadType == LoadType.REFRESH) {
//                    redditPagePostDao.deleteRedditPagePostsForRefresh("$redditPageName $redditPageSortOrder")
//                    redditPageNameRemoteKeyDao.deleteRemoteKeys("$redditPageName $redditPageSortOrder")
//                    println("HELLO IS THIS DELETED")
//                }
//
//                redditPageNameRemoteKeyDao.insert(RedditPageNameRemoteKey("$redditPageName $redditPageSortOrder", responseData.after))
//                redditPagePostDao.insertRedditPagePosts(redditPagePostsList)
//            }
//
//            return MediatorResult.Success(endOfPaginationReached = redditChildrenDataList.isEmpty())
//        } catch (e: IOException) {
//            return MediatorResult.Error(e)
//        } catch (e: HttpException) {
//            return MediatorResult.Error(e)
//        }
//        println("HELLO WORLD Error")
//    }

//    TODO infinite reload at subreddit page with no posts



    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RedditPagePost>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // Query DB for SubredditRemoteKey for the subreddit.
                    // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the Reddit API to fetch the next or previous page.
                    val remoteKey = redditPagePostDatabase.withTransaction {
                        redditPageNameRemoteKeyDao.getRemoteKeys("$redditPageName $redditPageSortOrder")
                    }

                    // We must explicitly check if the page key is null when appending, since the
                    // Reddit API informs the end of the list by returning null for page key, but
                    // passing a null key to Reddit API will fetch the initial page.
                    if (remoteKey.nextPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextPageKey
                }
            }
            println("HELLO123 ${state.config.initialLoadSize}")
            println("HELLO123 ${state.config.pageSize}")

            val responseData = if(redditPageName == "Home Feed") {
                redditApiService.getHomePosts(
                    User_Agent = "snapshots-for-reddit",
                    sort = redditPageSortOrder,
                    limit = when (loadType) {
                        LoadType.REFRESH -> state.config.initialLoadSize
                        else -> state.config.pageSize
                    },
                    after = loadKey,
                    before = null,
                ).data
            }else {
                redditApiService.getSubredditPosts(
                    User_Agent = "snapshots-for-reddit",
                    pageType = "r",
                    subreddit = redditPageName,
                    sort = redditPageSortOrder,
                    limit = when (loadType) {
                        LoadType.REFRESH -> state.config.initialLoadSize
                        else -> state.config.pageSize
                    },
                    after = loadKey,
                    before = null,

                    ).data
            }



            val redditChildrenDataList = responseData!!.children.map { it.data }


            redditPagePostDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    redditPageNameRemoteKeyDao.deleteRemoteKeys("$redditPageName $redditPageSortOrder")
                    redditPagePostDao.deleteRedditPagePostsForRefresh("$redditPageName $redditPageSortOrder")

                }



                val lastQueryPosition = redditPagePostDao.getLastQueryPosition("$redditPageName $redditPageSortOrder") ?: 1
                var queryPosition = lastQueryPosition + 1

                val redditPagePostsList = redditChildrenDataList.map { redditChildrenData ->
                    RedditPagePost(
                        redditPageSortOrder = redditPageSortOrder,

                        isCompact = redditPageIsCompact,

                        redditPageNameAndSortOrder = "$redditPageName $redditPageSortOrder",
                        redditPageName = redditPageName,
                        name = redditChildrenData.name,
                        subreddit = redditChildrenData.subreddit,

                        selftext = redditChildrenData.selftext,

                        saved = redditChildrenData.saved,


                        likes = redditChildrenData.likes,

                        title = redditChildrenData.title,

                        subreddit_name_prefixed = redditChildrenData.subreddit_name_prefixed,

                        is_reddit_media_domain = redditChildrenData.is_reddit_media_domain,

                        score = redditChildrenData.score,


                        thumbnail = redditChildrenData.thumbnail,


                        is_self = redditChildrenData.is_self,


                        url_overridden_by_dest = redditChildrenData.url_overridden_by_dest,


                        archived = redditChildrenData.archived,

                        no_follow = redditChildrenData.no_follow,

                        is_crosspostable = redditChildrenData.is_crosspostable,

                        over_18 = redditChildrenData.over_18,



                        id = redditChildrenData.id,


                        author = redditChildrenData.author,


                        num_comments = redditChildrenData.num_comments,




                        //not used atm
                        permalink = redditChildrenData.permalink,


                        is_video = redditChildrenData.is_video,

                        body = redditChildrenData.body,

                        link_title = redditChildrenData.link_title,


                        created_utc = redditChildrenData.created_utc,


                        //mapped content

                        previewUrl = redditChildrenData.preview?.images?.get(0)?.source?.url,
                        previewWidth = redditChildrenData.preview?.images?.get(0)?.source?.width,
                        previewHeight = redditChildrenData.preview?.images?.get(0)?.source?.height,



//                        media: RedditPageMedia
                        sr_community_icon = redditChildrenData.sr_detail?.community_icon,
                        sr_icon_img = redditChildrenData.sr_detail?.icon_img,


                        //DEFAULTS
                        history_type = redditChildrenData.history_type,
                        history_name = redditChildrenData.history_name,
                        icon = redditChildrenData.icon,

                        //userinfo
                        user_name = redditChildrenData.user_name,
                        link_karma = redditChildrenData.link_karma,
                        comment_karma =redditChildrenData.comment_karma,
                        total_karma = redditChildrenData.total_karma,
                        awarder_karma = redditChildrenData.awarder_karma,
                        awardee_karma = redditChildrenData.awardee_karma,
                        user_created_utc = redditChildrenData.user_created_utc,

                        queryPosition = queryPosition++
                        )

                }


                val searchBar = listOf(RedditPagePost(dataKind = "searchBar", name = "searchBar $redditPageName $redditPageSortOrder",redditPageNameAndSortOrder  = "$redditPageName $redditPageSortOrder", redditPageName = redditPageName, redditPageSortOrder = redditPageSortOrder, queryPosition = 0))


                redditPagePostDao.insertRedditPagePosts(searchBar + redditPagePostsList)
                redditPageNameRemoteKeyDao.insert(RedditPageNameRemoteKey("$redditPageName $redditPageSortOrder", responseData.after))
                println("HELLO123 ${responseData.after}")
            }

            return MediatorResult.Success(endOfPaginationReached = redditChildrenDataList.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
        println("HELLO WORLD Error")
    }


}
