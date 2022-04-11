package com.example.snapshotsforreddit.ui.tabs.home

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.repository.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
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
    val subreddits = _accessToken.switchMap { redditApiRepository.getSubscribedResults().cachedIn(viewModelScope)

    }
    fun checkIfAccessTokenChanged(accessToken: String) = viewModelScope.launch {
        //only if accessToken changes do we update subreddits
        if (_accessToken.value != accessToken) {
            _accessToken.value = accessToken

            //save the username in datastore. //if unable to get username, the user must relog because refresh token expired or an error with login
            try {
                val request = redditApiRepository.getUsername().name
                Log.v(TAG, "HELLO $request")
            } catch (e: Exception) {

            }
        }
        Log.v(TAG, "HELLO $accessToken")

    }

    private fun getLoggedInUsername() = viewModelScope.launch {
        try {
            val request = redditApiRepository.getUsername().name
            Log.v(TAG, "HELLO $request")
        } catch (e: Exception) {

        }
    }




}