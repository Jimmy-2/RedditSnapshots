package com.example.snapshotsforreddit.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("test_preferences")

@Singleton
class TestDataStoreRepository @Inject constructor(@ApplicationContext context: Context)  {

    private val testDataStore = context.dataStore


    val currentUsername = testDataStore.data.map { it[CURRENT_USERNAME] ?: "Anonymous" }

    suspend fun updateCurrentUsername(currentUsername: String) {
        println("HELLO UPDATING username ${currentUsername}")
        testDataStore.edit { preferences ->
            preferences[CURRENT_USERNAME] = currentUsername
        }
    }



    private val CURRENT_USERNAME = stringPreferencesKey("current_username")

}