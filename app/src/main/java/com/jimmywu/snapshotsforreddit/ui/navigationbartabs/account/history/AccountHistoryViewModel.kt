package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.account.history

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.jimmywu.snapshotsforreddit.data.datastore.AuthDataStoreRepository
import com.jimmywu.snapshotsforreddit.network.RedditApiRepository
import com.jimmywu.snapshotsforreddit.network.responses.account.UserInfo
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
    private val isCompact = MutableLiveData<Boolean?>()

    val accountHistoryItems = _currentUserHistory.switchMap { userHistory ->
        redditApiRepository.getUserPostsList(userHistory.username, userHistory.historyType, isCompact.value).cachedIn(viewModelScope)

    }

    fun selectedUserHistory(newUserHistory: UserHistory) {
        if(_currentUserHistory.value != newUserHistory) {
            _currentUserHistory.value = newUserHistory
        }

    }
}