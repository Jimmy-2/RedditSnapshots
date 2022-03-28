package com.example.snapshotsforreddit.network.responses.subscribed

import com.example.snapshotsforreddit.network.responses.ChildrenObject

data class SubscribedData (
    val after: String?,
    //val dist: Int?,
    //val modhash: String?,
    //val geo_filter: String?,
    val children: List<SubscribedChildrenObject>,
    val before: String?
)
