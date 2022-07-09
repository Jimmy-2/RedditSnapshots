package com.jimmywu.snapshotsforreddit.network.responses.redditpostdetails

import com.squareup.moshi.*

class RepliesAdapter {
    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<RepliesData.RedditPostCommentsReplies>): RepliesData? {
        return if (jsonReader.peek() == JsonReader.Token.BEGIN_OBJECT) {
            delegate.fromJson(jsonReader)
        } else {
            RepliesData.StringData(jsonReader.nextString())
        }
    }

    @ToJson
    fun toJson(jsonWriter: JsonWriter, data: RepliesData, delegate: JsonAdapter<RepliesData.RedditPostCommentsReplies>) {
        when (data) {
            is RepliesData.RedditPostCommentsReplies -> delegate.toJson(jsonWriter, data)
            is RepliesData.StringData -> jsonWriter.value(data.value)
        }
    }
}


