package com.example.snapshotsforreddit.ui

import androidx.lifecycle.ViewModel
import com.example.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
) :
    ViewModel() {

//    private val refreshSignal = MutableSharedFlow<Unit>()
//
//    private val loadDataSignal: Flow<Unit> = flow {
//        emit(Unit)
//        emitAll(refreshSignal)
//    }
//
//    val currentTheme: AppTheme = {
//
//    }
//

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
