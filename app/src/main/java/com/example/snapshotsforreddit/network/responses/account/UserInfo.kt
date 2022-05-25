package com.example.snapshotsforreddit.network.responses.account


data class UserInfo (
    val name: String?,
    val link_karma: Int?,
    val comment_karma: Int?,
    val total_karma: Int?,
    val awarder_karma: Int?,
    val awardee_karma: Int?,

    val created_utc: Long?
    )
