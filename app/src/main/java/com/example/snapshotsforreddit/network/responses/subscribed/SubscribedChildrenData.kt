package com.example.snapshotsforreddit.network.responses.subscribed

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscribedChildrenData(
    @Json(name = "display_name")
    val displayName: String?,

    @Json(name = "display_name_prefixed")
    val displayNamePrefixed: String?,


    val headerImg: String?,
    @Json(name = "header_size")


    val headerTitle: String?,
    @Json(name = "hide_ads")

    val hideAds: Boolean?,
    @Json(name = "icon_img")

    val iconImg: String?,
    @Json(name = "icon_size")

    val iconSize: Any?,
    @Json(name = "id")
    val id: String?,


    @Json(name = "name")
    val name: String?,


    @Json(name = "over18")
    val over18: Boolean?,


    @Json(name = "quarantine")
    val quarantine: Boolean?,


    @Json(name = "restrict_commenting")
    val restrictCommenting: Boolean?,
    @Json(name = "restrict_posting")
    val restrictPosting: Boolean?,


    @Json(name = "subreddit_type")
    val subredditType: String?,


    @Json(name = "title")
    val title: String?,
    @Json(name = "url")
    val url: String?,


    @Json(name = "user_has_favorited")
    val userHasFavorited: Boolean?,
    @Json(name = "user_is_banned")
    val userIsBanned: Boolean?,
    @Json(name = "user_is_contributor")
    val userIsContributor: Boolean?,
    @Json(name = "user_is_moderator")
    val userIsModerator: Boolean?,
    @Json(name = "user_is_muted")
    val userIsMuted: Boolean?,

    @Json(name = "user_is_subscriber")
    val userIsSubscriber: Boolean?,


    )
