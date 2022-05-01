package com.example.snapshotsforreddit.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("data_store")

@Singleton
class TokensDatastore  @Inject constructor (@ApplicationContext context: Context) {

    private val TAG: String = "TokensDatastore"

    private val authDataStore = context.dataStore

    //retrieve value
    val readTokensFromDataStore: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                //exception.printStackTrace()
                Log.e(TAG, "Error reading token preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
        preferences[ACCESS_TOKEN] ?: ""
    }



    suspend fun saveAccessTokenToDataStore(accessToken: String) {
        //store new access token string value
        //write to datastore
        authDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }


}