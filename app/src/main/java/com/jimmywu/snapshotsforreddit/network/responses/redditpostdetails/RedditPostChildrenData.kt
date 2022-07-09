package com.jimmywu.snapshotsforreddit.network.responses.redditpostdetails

data class RedditPostChildrenData(
    //More comments

    val count: Int?,
    //list of ids for the additional comments in the reddit post. Need to append t1_ to get full name id
    val children: List<String>?,


    ///////////////////////////////////////////////////////////////A singular comment and its attributes///////////////////////////////////////////////////////////////////////////
    val subreddit_id: String?,

    val author_is_blocked: Boolean?,

    val total_awards_received: Int?,

    val subreddit: String?,

    val likes: Boolean?,

    //recursive tree
//    val replies: Any?,
//    val replies: RedditPostCommentsDetail?,

    val replies: RepliesData?,

    val saved: Boolean?,
    val id: String?,

    val gilded: Int?,
    val archived: Boolean?,

    val no_follow: Boolean?,
    val author: String?,
    val can_mod_post: Boolean?,
    val created_utc: Long?,
    val send_replies: Boolean?,
    val parent_id: String?,
    val score: Int?,
    val author_fullname: String?,


    val collapsed: Boolean?,
    val body: String?,
//    val edited: Boolean?,

    val name: String?,


    val is_submitter: Boolean?,


    val author_patreon_flair: Boolean?,
    val body_html: String?,

    val stickied: Boolean?,
    val author_premium: Boolean?,
    val can_gild: Boolean?,

//val gildings


    val score_hidden: Boolean?,
    val permalink: String?,
    val subreddit_type: String?,
    val locked: Boolean?,

    val created: Long?,


    val link_id: String?,

    val subreddit_name_prefixed: String?,
    val controversiality: Int?,

    //depth of tree. Comments with depth 0 are direct replies to the original post.
    val depth: Int = 0,


    val ups: Int?,


    )


//data class RedditPostCommentsReplies(val kind: String?, val data: RedditPostCommentsData)



