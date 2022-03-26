package com.example.snapshotsforreddit

import android.app.Application
import com.example.snapshotsforreddit.data.room.PostRoomDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
    val database: PostRoomDatabase by lazy {
        PostRoomDatabase.getDatabase(this)
    }
}
