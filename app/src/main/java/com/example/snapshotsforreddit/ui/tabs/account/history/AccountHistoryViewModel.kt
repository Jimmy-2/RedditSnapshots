package com.example.snapshotsforreddit.ui.tabs.account.history

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.network.AuthApiRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountHistoryViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    private val _username = MutableLiveData<String>()
    private val _userData = MutableLiveData<UserInfo?>()
    private val _pageType = MutableLiveData<String>()

    val accountHistoryItems = _username.switchMap { username ->
        redditApiRepository.getUserPostsList(username, _userData.value, _pageType.value?: "overview").cachedIn(viewModelScope)

    }



    fun selectedUserHistory(historyType: String, username: String) {
        _pageType.value = historyType
        _username.value = username
    }
}