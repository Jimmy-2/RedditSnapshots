package com.example.snapshotsforreddit.ui.general.redditpage

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.Repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.Repository.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedditPageViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    private val _accessToken = MutableLiveData<String>()

    val redditPagePosts = _accessToken.switchMap { accessT ->
        redditApiRepository.getSubredditPostsList(accessT, _subredditName.value!!, _subredditType.value!!).cachedIn(viewModelScope)

    }
    fun checkIfAccessTokenChanged(accessToken: String) = viewModelScope.launch {
        //only if accessToken changes do we update subreddits
        if (_accessToken.value != accessToken) {
            _accessToken.value = accessToken

            //save the username in datastore. //if unable to get username, the user must relog because refresh token expired or an error with login
            getLoggedInUsername(accessToken)
        }
    }
    
    private fun getLoggedInUsername(accessToken: String) = viewModelScope.launch {
        try {
            val request = redditApiRepository.getUsername(accessToken).name
        } catch (e: Exception) {

        }
    }

    private val _subredditName= MutableLiveData<String>()
    val subredditName: LiveData<String> = _subredditName
    private val _subredditType= MutableLiveData<String>()
    val subredditType : LiveData<String> = _subredditType

    fun redditPageInformation(redditPageName: String, redditPageType: String) {
        _subredditName.value = redditPageName
        _subredditType.value = redditPageType
    }


}