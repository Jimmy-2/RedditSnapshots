package com.example.snapshotsforreddit.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.snapshotsforreddit.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class PostRoomDatabase : RoomDatabase() {

    //use dependency injection to use this dao in where we need it
    abstract fun postDao(): PostDao


    //provide dummy entries for room
    class Callback @Inject constructor(
        //lazily instantitate on the database.get() function below
        private val database: Provider<PostRoomDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            //onCreate is executed after the providedatabase .build() method finishes
            super.onCreate(db)

            val dao = database.get().postDao()

            //now we can use this dao to do the database operations
            //since they are suspend functions, we have to call them from a coroutine
            //we also have to scope the coroutine so it knows when to stop,
            //we need to create a coroutine scope that runs as long as the app is running
            applicationScope.launch {
                dao.insert(Post("post1","zzz", "sad"))
                dao.insert(Post("post2","y", "ds"))
                dao.insert(Post("post3","x", "ds", true))
                dao.insert(Post("post4","f", "ds"))

            }

        }
    }


    //put this into di module
    companion object {
        @Volatile
        private var INSTANCE: PostRoomDatabase? = null

        fun getDatabase(context: Context): PostRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostRoomDatabase::class.java,
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