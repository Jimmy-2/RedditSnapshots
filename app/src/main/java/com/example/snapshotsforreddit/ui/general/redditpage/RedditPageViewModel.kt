package com.example.snapshotsforreddit.ui.general.redditpage

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.repository.RedditApiRepository
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

    //TODO CHANGE THIS
    val redditPagePosts = _accessToken.switchMap { accessT ->
        redditApiRepository.getSubredditPostsList(_subredditName.value!!, _subredditType.value!!).cachedIn(viewModelScope)

    }


    fun checkIfAccessTokenChanged(accessToken: String) = viewModelScope.launch {
        //only if accessToken changes do we update subreddits
        if (_accessToken.value != accessToken) {
            _accessToken.value = accessToken

        }
    }
    

    private val _subredditName= MutableLiveData<String>()
    private val _subredditType= MutableLiveData<String>()

    fun redditPageInformation(redditPageName: String, redditPageType: String) {
        _subredditName.value = redditPageName
        _subredditType.value = redditPageType
    }


}