package com.example.snapshotsforreddit.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class TokensRepository(private val dataStore: DataStore<Preferences>) {
    private val TAG: String = "TokensRepository"

    private val ACCESS_TOKEN = stringPreferencesKey("access_token")

    private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    //read from datastore to obtain token values_values
    val readTokensFromDataStore: Flow<String> = dataStore.data
        //error handling
        .catch { exception ->
            if (exception is IOException) {
                //exception.printStackTrace()
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            //TODO: create some logic for accessing the token on the first run before authenticating and when a token expires
            preferences[ACCESS_TOKEN] ?: ""

        }//TODO: add logic to read and retrieve refresh token



    //transactionally updates the data
    suspend fun saveAccessTokenToDataStore(accessToken: String) {
        //store new access token string value
        //write to datastore
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }




}