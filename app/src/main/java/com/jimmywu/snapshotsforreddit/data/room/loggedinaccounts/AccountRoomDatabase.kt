package com.jimmywu.snapshotsforreddit.data.room.loggedinaccounts

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jimmywu.snapshotsforreddit.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Account::class], version = 1, exportSchema = false)
abstract class AccountRoomDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao

    //provide a starting account to use (anonymous)
    class Callback @Inject constructor(

        private val database: Provider<AccountRoomDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            //onCreate is executed after the providedatabase .build() method finishes
            super.onCreate(db)

            val dao = database.get().accountDao()

            //now we can use this dao to do the database operations
            //since they are suspend functions, we have to call them from a coroutine
            //we also have to scope the coroutine so it knows when to stop,
            //we need to create a coroutine scope that runs as long as the app is running
            applicationScope.launch {
//                dao.insert(Account("Anonymous","", ""))


            }

        }
    }

}