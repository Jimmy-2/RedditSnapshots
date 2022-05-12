package com.example.snapshotsforreddit.ui

import androidx.lifecycle.ViewModel
import com.example.snapshotsforreddit.data.AppTheme
import com.example.snapshotsforreddit.data.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.di.ApplicationScope
import com.example.snapshotsforreddit.ui.tabs.settings.themeFromPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
) :
    ViewModel() {

    private val refreshSignal = MutableSharedFlow<Unit>()

    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }


    val selectedTheme = preferencesDataStoreRepository.selectedTheme

    val theme: StateFlow<AppTheme> = loadDataSignal.mapLatest {
        getTheme()
    }.stateIn(applicationScope, SharingStarted.Eagerly, AppTheme.SYSTEM)


    private suspend fun getTheme(): AppTheme {
        val selectedTheme: String = preferencesDataStoreRepository.selectedTheme.first()
        return themeFromPreferences(selectedTheme) ?: AppTheme.SYSTEM
    }


}
