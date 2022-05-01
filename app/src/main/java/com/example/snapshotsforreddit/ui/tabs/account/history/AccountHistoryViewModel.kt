package com.example.snapshotsforreddit.ui.tabs.account.history

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.AuthDataStoreRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class UserHistory(val historyType: String, val username: String)

@HiltViewModel
class AccountHistoryViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    val authFlow = authDataStoreRepository.authFlow.asLiveData()


    private val _userData = MutableLiveData<UserInfo?>()
    private val _currentUserHistory = MutableLiveData<UserHistory>()

    val accountHistoryItems = _currentUserHistory.switchMap { userHistory ->
        redditApiRepository.getUserPostsList(userHistory.username, _userData.value, userHistory.historyType).cachedIn(viewModelScope)

    }

    fun selectedUserHistory(newUserHistory: UserHistory) {
        if(_currentUserHistory.value != newUserHistory) {
            _currentUserHistory.value = newUserHistory
        }

    }
}