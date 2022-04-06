package com.example.snapshotsforreddit.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


enum class SortOrder { BY_TITLE, BY_DATE, BY_SUBREDDIT }

data class FilterPreferences(val sortOrder: SortOrder, val isCompactView: Boolean)

private val Context.dataStore by preferencesDataStore("user_preferences")

//this repository belongs to the entire activity and we only need 1 instance of it running (used in downloaded posts screen)
@Singleton
class PreferencesRepository @Inject constructor(@ApplicationContext context: Context) {
    //dagger injects context automatically

    private val TAG: String = "PreferencesRepository"

    private val preferencesDataStore = context.dataStore

    val preferencesFlow = preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                //something went wrong while reading the data
                Log.e(TAG, "Error reading token preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            )
            val isCompactView = preferences[PreferencesKeys.IS_COMPACT_VIEW] ?: false
            FilterPreferences(sortOrder, isCompactView)
        }


    suspend fun updateSortOrder(sortOrder: SortOrder) {
        preferencesDataStore.edit {
            //.name because we cannot store an enum, so we have to convert to string
                preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name

        }
    }

    suspend fun updateIsCompactView(isCompactView: Boolean) {
        preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_COMPACT_VIEW] = isCompactView
        }
    }


    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val IS_COMPACT_VIEW = booleanPreferencesKey("is_compact_view")
    }
}