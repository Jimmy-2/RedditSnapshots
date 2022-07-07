package com.jimmywu.snapshotsforreddit.ui

import androidx.lifecycle.ViewModel
import com.jimmywu.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.jimmywu.snapshotsforreddit.di.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun getSelectedTheme(): String =
        runBlocking { preferencesDataStoreRepository.selectedTheme.first() }

    val selectedTheme = preferencesDataStoreRepository.selectedTheme



}

