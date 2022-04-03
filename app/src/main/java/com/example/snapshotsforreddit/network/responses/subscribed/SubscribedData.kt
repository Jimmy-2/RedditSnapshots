package com.example.snapshotsforreddit.network.responses.subscribed


data class SubscribedData (
    val after: String?,
    //val dist: Int?,
    //val modhash: String?,
    //val geo_filter: String?,
    val children: List<SubscribedChildrenObject>,
    val before: String?
)
