package com.jimmywu.snapshotsforreddit.network.responses.redditpostdetails

import com.squareup.moshi.JsonClass

sealed class RepliesData {
    data class StringData(val value: String): RepliesData()

    @JsonClass(generateAdapter = true)
    data class RedditPostCommentsReplies(
        val kind: String?, val data: RedditPostCommentsData
    ) : RepliesData()


}






