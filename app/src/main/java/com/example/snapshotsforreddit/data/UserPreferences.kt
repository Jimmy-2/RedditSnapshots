package com.example.snapshotsforreddit.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore("data_store")

class UserPreferences(private val context: Context) {
    //retrieve value
    val readTokensFromDataStore: Flow<String> = context.dataStore.data
        .map { preferences ->
        // On the first run of the app, we will use LinearLayoutManager by default
        preferences[ACCESS_TOKEN] ?: ""
    }



    suspend fun saveAccessTokenToDataStore(accessToken: String) {
        //store new access token string value
        //write to datastore
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }


}