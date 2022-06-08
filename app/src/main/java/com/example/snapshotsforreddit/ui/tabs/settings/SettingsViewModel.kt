/* THIS FILE WAS MODIFIED FROM ITS ORIGINAL VERSION BY JIMMY WU FOR REDDIT SNAPSHOTS.
   CODE LINK: https://github.com/google/iosched
   THE ORIGINAL LICENSE IS STATED BELOW.

 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.snapshotsforreddit.ui.tabs.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapshotsforreddit.data.datastore.AppTheme
import com.example.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.util.tryOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
) : ViewModel() {

    private val refreshSignal = MutableSharedFlow<Unit>()

    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }


    val theme: StateFlow<AppTheme> = loadDataSignal.mapLatest {
        getTheme()
    }.stateIn(viewModelScope, WhileViewSubscribed, AppTheme.SYSTEM)


    val availableThemes: StateFlow<List<AppTheme>> = loadDataSignal.mapLatest {
        listOf(AppTheme.LIGHT, AppTheme.DARK, AppTheme.SYSTEM)
    }.stateIn(viewModelScope, WhileViewSubscribed, listOf())


    private val _navigationActions = Channel<SettingsNavigationAction>(capacity = Channel.CONFLATED)


    val navigationActions = _navigationActions.receiveAsFlow()

    private suspend fun getTheme(): AppTheme {
        val selectedTheme: String = preferencesDataStoreRepository.selectedTheme.first()
        return themeFromPreferences(selectedTheme)?: AppTheme.SYSTEM
    }



    fun onThemeSelected(theme: AppTheme) = viewModelScope.launch{
        preferencesDataStoreRepository.updateAppTheme(theme)
    }


    fun onChangeColorClicked() {
        _navigationActions.tryOffer(SettingsNavigationAction.NavigateToThemeSelector)
    }
}



sealed class SettingsNavigationAction {
    object NavigateToThemeSelector : SettingsNavigationAction()
}

fun themeFromPreferences(storageKey: String): AppTheme? {
    return AppTheme.values().firstOrNull { it.name == storageKey }
}


val WhileViewSubscribed: SharingStarted = SharingStarted.WhileSubscribed(5000)



