package com.example.snapshotsforreddit.ui

import androidx.lifecycle.ViewModel
import com.example.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
) :
    ViewModel() {

    fun getSelectedTheme(): String = runBlocking{ preferencesDataStoreRepository.selectedTheme.first() }

    val selectedTheme = preferencesDataStoreRepository.selectedTheme








//    val theme: StateFlow<AppTheme> = loadDataSignal.mapLatest {
//        getTheme()
//    }.stateIn(applicationScope, SharingStarted.Eagerly, AppTheme.SYSTEM)
//
//
//    private suspend fun getTheme(): AppTheme {
//        val selectedTheme: String = preferencesDataStoreRepository.selectedTheme.first()
//        return themeFromPreferences(selectedTheme) ?: AppTheme.SYSTEM
//    }


}

