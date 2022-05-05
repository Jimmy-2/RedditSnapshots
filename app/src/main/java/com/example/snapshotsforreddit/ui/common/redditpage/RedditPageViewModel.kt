package com.example.snapshotsforreddit.ui.common.redditpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.util.MonitorTriple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedditPageViewModel @Inject constructor(
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    //read and update isCompact value
    val preferencesFlow = preferencesDataStoreRepository.preferencesFlow
    val subredditName = MutableLiveData<String>()
    private val subredditType = MutableLiveData<String>()
    private val isCompact = MutableLiveData<Boolean?>()
    private val sortOrder = MutableLiveData<String?>()


//    val redditPagePosts = subredditInfo.switchMap {
//        redditApiRepository.getSubredditPostsList(subredditName.value!!, subredditType.value!!, it.sortOrder, it.isCompactView).cachedIn(viewModelScope)
//    }

    //if isCompact or sortOrder value changes, we will update the screen
    val redditPagePosts = Transformations.switchMap(MonitorTriple(isCompact, sortOrder, subredditName)) {
        redditApiRepository.getSubredditPostsList(
            it.third!!, subredditType.value!!,
            it.second, it.first).cachedIn(viewModelScope)
    }

    fun onVoteOnPost(typeOfVote: Int, post: RedditChildrenObject) = viewModelScope.launch {
        try {
            post.data?.name?.let { redditApiRepository.voteOnThing(typeOfVote, it) }
        }
        catch (e: Exception) {

        }

    }

    fun onSortOrderSelected(newSortOrder: String) {
        if(sortOrder.value != newSortOrder) {
            sortOrder.value = newSortOrder
        }
    }
    fun onCompactViewClicked(isCompactView: Boolean) = viewModelScope.launch{
        //update isCompactView in datastore on compact button clicked
        preferencesDataStoreRepository.updateIsCompactView(isCompactView)
        checkIsCompact(isCompactView)

    }

    fun checkIsCompact(newVal : Boolean) {
        if(isCompact.value != newVal) {
            isCompact.value = newVal
        }

    }

    fun loadRedditPage(redditPageName: String, redditPageType: String) {
        if(subredditName.value == null || subredditName.value == "") {
            subredditName.value = redditPageName
            subredditType.value = redditPageType
        }


    }

    fun changeRedditPage(redditPageName: String, redditPageType: String) {
        if(subredditName.value != redditPageName) {
            subredditName.value = redditPageName
        }
        if(subredditType.value != redditPageType) {
            subredditType.value = redditPageType
        }
    }


}
