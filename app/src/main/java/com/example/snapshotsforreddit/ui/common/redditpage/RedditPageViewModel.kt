package com.example.snapshotsforreddit.ui.common.redditpage

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
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

    private val _subredditName = MutableLiveData<String>()
    val subredditName: LiveData<String> = _subredditName

    private val subredditType = MutableLiveData<String>()
    private val isCompact = MutableLiveData<Boolean?>()
    private val sortOrder = MutableLiveData<String?>()
    private val isDefault = MutableLiveData<Boolean?>()


//    val redditPagePosts = subredditInfo.switchMap {
//        redditApiRepository.getSubredditPostsList(subredditName.value!!, subredditType.value!!, it.sortOrder, it.isCompactView).cachedIn(viewModelScope)
//    }

    //if isCompact or sortOrder value changes, we will update the screen
//    val redditPagePosts = Transformations.switchMap(MonitorTriple(isCompact, sortOrder, subredditName)) {
//        redditApiRepository.getSubredditPostsList(
//            it.third!!, subredditType.value!!,
//            it.second, it.first).cachedIn(viewModelScope)
//    }

    val redditPagePosts = Transformations.switchMap(MonitorTriple(isCompact, sortOrder, _subredditName)) {
        redditApiRepository.getSubredditPostsList(
            it.third!!, subredditType.value!!, isDefault.value,
            it.second, it.first).cachedIn(viewModelScope)


    }


    fun onVoteOnPost(typeOfVote: Int, post: RedditChildrenData) = viewModelScope.launch {
        try {
            post.name?.let { redditApiRepository.voteOnThing(typeOfVote, it) }
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

    fun loadRedditPage(redditPageName: String, redditPageType: String, default: Boolean) {
        if(_subredditName.value == null || _subredditName.value == "") {
            _subredditName.value = redditPageName
            subredditType.value = redditPageType
            isDefault.value = default
        }
    }

    fun changeRedditPage(redditPageName: String, redditPageType: String) {
        isDefault.value = false
        if(_subredditName.value != redditPageName) {
            _subredditName.value = redditPageName
        }
        if(subredditType.value != redditPageType) {
            subredditType.value = redditPageType
        }

    }

}
