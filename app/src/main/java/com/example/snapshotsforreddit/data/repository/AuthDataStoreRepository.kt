package com.example.snapshotsforreddit.data.repository

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

data class FilterAuth(val accessToken: String, val refreshToken: String, val loginState: Boolean, val username: String)

private val Context.dataStore by preferencesDataStore("tokens_datastore")

@Singleton
class AuthDataStoreRepository @Inject constructor (@ApplicationContext context: Context) {
    private val TAG: String = "AuthDataStoreRepository"

    private val authDataStore = context.dataStore

    //read from datastore to obtain token values
    val authFlow = authDataStore.data
        //error handling
        .catch { exception ->
            if (exception is IOException) {
                //exception.printStackTrace()
                Log.e(TAG, "Error reading auth values.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val accessToken = preferences[AuthKeys.ACCESS_TOKEN] ?: ""
            val refreshToken = preferences[AuthKeys.REFRESH_TOKEN] ?: ""
            val loginState = preferences[AuthKeys.LOGIN_STATE] ?: false
            val username = preferences[AuthKeys.USERNAME] ?: ""
            FilterAuth(accessToken,refreshToken,loginState, username)
        }




    //transactionally updates the data

    suspend fun updateAccessToken(accessToken: String) {
        //store new access token string value
        //write to datastore
        authDataStore.edit { preferences ->
            preferences[AuthKeys.ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun updateRefreshToken(refreshToken: String) {
        //store new refresh token string value
        //write to datastore
        authDataStore.edit { preferences ->
            preferences[AuthKeys.REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun updateLoginState(loginState: Boolean) {
        //store new refresh token string value
        //write to datastore
        authDataStore.edit { preferences ->
            preferences[AuthKeys.LOGIN_STATE] = loginState
        }
    }

    suspend fun updateUsername(username: String) {
        //store new refresh token string value
        //write to datastore
        authDataStore.edit { preferences ->
            preferences[AuthKeys.USERNAME] = username
        }
    }






    private object AuthKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val LOGIN_STATE = booleanPreferencesKey("login_state")
        val USERNAME = stringPreferencesKey("username")
    }


}