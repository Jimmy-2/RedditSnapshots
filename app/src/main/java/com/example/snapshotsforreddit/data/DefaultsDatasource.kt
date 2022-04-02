package com.example.snapshotsforreddit.data

import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenData
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject

class DefaultsDatasource {
    fun loadDefaultSubreddits(): List<SubscribedChildrenObject> {
        return listOf(
            SubscribedChildrenObject(kind = null, data = SubscribedChildrenData("Home", "Home", null, null, false)),
            SubscribedChildrenObject(kind = null, data = SubscribedChildrenData("Popular", "r/Popular", null, null, false)),
            SubscribedChildrenObject(kind = null, data = SubscribedChildrenData("All", "r/All", null, null, false)),
        )
    }
}