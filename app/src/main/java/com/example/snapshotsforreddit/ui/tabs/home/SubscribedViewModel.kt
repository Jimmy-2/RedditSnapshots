package com.example.snapshotsforreddit.ui.tabs.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.repository.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribedViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {
    private val TAG: String = "SubscribedViewModel"

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    private val _accessToken = MutableLiveData<String>()

    val subreddits = _accessToken.switchMap {
        redditApiRepository.getSubscribedSubredditsList().cachedIn(viewModelScope)
    }

    fun checkIfAccessTokenChanged(accessToken: String) = viewModelScope.launch {
        //only if accessToken changes do we update subreddits
        if (_accessToken.value != accessToken) {
            _accessToken.value = accessToken
        }
    }


}