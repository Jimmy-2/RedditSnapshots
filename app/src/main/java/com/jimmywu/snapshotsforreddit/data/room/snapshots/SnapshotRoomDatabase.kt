package com.jimmywu.snapshotsforreddit.data.room.snapshots

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jimmywu.snapshotsforreddit.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Snapshot::class], version = 1, exportSchema = false)
abstract class SnapshotRoomDatabase : RoomDatabase() {

    //use dependency injection to use this dao in where we need it
    abstract fun snapshotDao(): SnapshotDao


    //provide dummy entries for room
    class Callback @Inject constructor(
        //lazily instantitate on the database.get() function below
        private val database: Provider<SnapshotRoomDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            //onCreate is executed after the providedatabase .build() method finishes
            super.onCreate(db)

            val dao = database.get().snapshotDao()

            //now we can use this dao to do the database operations
            //since they are suspend functions, we have to call them from a coroutine
            //we also have to scope the coroutine so it knows when to stop,
            //we need to create a coroutine scope that runs as long as the app is running
            applicationScope.launch {
                dao.insert(Snapshot("post1","zzz", "sad"))
                dao.insert(Snapshot("post2","y", "ds"))
                dao.insert(Snapshot("post3","x", "ds", true))
                dao.insert(Snapshot("post4","f", "ds"))

            }

        }
    }


    //put this into di module
    companion object {
        @Volatile
        private var INSTANCE: SnapshotRoomDatabase? = null

        fun getDatabase(context: Context): SnapshotRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SnapshotRoomDatabase::class.java,
                    "post_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }


    }
}