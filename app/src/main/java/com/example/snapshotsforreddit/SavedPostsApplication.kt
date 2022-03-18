package com.example.snapshotsforreddit

import android.app.Application
import com.example.snapshotsforreddit.database.PostRoomDatabase

class SavedPostsApplication: Application() {
    val database: PostRoomDatabase by lazy {
        PostRoomDatabase.getDatabase(this)
    }
}
