package com.example.snapshotsforreddit.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class Tokens(val accessToken: String, val refreshToken: String)

private val Context.dataStore by preferencesDataStore("tokens_datastore")

@Singleton
class TokensRepository @Inject constructor (@ApplicationContext context: Context) {
    private val TAG: String = "TokensRepository"

    private val TokensDataStore = context.dataStore

    //read from datastore to obtain token values
    val tokensFlow = TokensDataStore.data
        //error handling
        .catch { exception ->
            if (exception is IOException) {
                //exception.printStackTrace()
                Log.e(TAG, "Error reading token values.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val accessToken = preferences[TokenKeys.ACCESS_TOKEN] ?: ""
            val refreshToken = preferences[TokenKeys.REFRESH_TOKEN] ?: ""
            Tokens(accessToken,refreshToken)
        }



    //transactionally updates the data
    suspend fun updateAccessToken(accessToken: String) {
        //store new access token string value
        //write to datastore
        TokensDataStore.edit { preferences ->
            preferences[TokenKeys.ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun updateRefreshToken(refreshToken: String) {
        //store new refresh token string value
        //write to datastore
        TokensDataStore.edit { preferences ->
            preferences[TokenKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    private object TokenKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }


}