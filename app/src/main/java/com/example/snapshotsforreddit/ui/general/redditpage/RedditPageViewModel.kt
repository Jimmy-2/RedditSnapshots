package com.example.snapshotsforreddit.ui.general.redditpage

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedditPageViewModel @Inject constructor(
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    val preferencesFlow = preferencesDataStoreRepository.preferencesFlow

    private val subredditName = MutableLiveData<String>()
    private val subredditType = MutableLiveData<String>()
    private val isCompact = MutableLiveData<Boolean?>()

    //TODO CHANGE THIS
    val redditPagePosts = isCompact.switchMap {
        redditApiRepository.getSubredditPostsList(subredditName.value!!, subredditType.value!!, null, it).cachedIn(viewModelScope)

    }

    fun onVoteOnPost(typeOfVote: Int, post: RedditChildrenObject) = viewModelScope.launch {
        try {
            post.data?.name?.let { redditApiRepository.voteOnThing(typeOfVote, it) }
        }
        catch (e: Exception) {

        }

    }

    fun onCompactViewClicked(isCompactView: Boolean) = viewModelScope.launch{
        //update iscompactview in datastore on compact button clicked
        preferencesDataStoreRepository.updateIsCompactView(isCompactView)
        checkIsCompact(isCompactView)

    }

    fun checkIsCompact(newVal: Boolean) {
        if(isCompact.value != newVal) {
            isCompact.value = newVal
        }

    }

    fun changeRedditPage(redditPageName: String, redditPageType: String) {
        subredditName.value = redditPageName
        subredditType.value = redditPageType
    }


}