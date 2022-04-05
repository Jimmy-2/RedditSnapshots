package com.example.snapshotsforreddit.ui.tabs.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapshotsforreddit.data.Repository.AuthApiRepository
import com.example.snapshotsforreddit.data.Repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.Repository.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {
    private fun getAccessToken(code: String) = viewModelScope.launch {
        try {
            onAccessTokensUpdated(authApiRepository.getTokenValues(code))
        } catch (e: Exception) {

        }

    }

}

 */