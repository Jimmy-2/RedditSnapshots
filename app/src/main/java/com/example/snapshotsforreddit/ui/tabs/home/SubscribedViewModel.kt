package com.example.snapshotsforreddit.ui.tabs.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.snapshotsforreddit.data.datastore.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.room.cache.SubscribedSubreddit
import com.example.snapshotsforreddit.data.room.cache.SubscribedSubredditRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribedViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val redditApiRepository: RedditApiRepository,
    private val subscribedSubredditRepository: SubscribedSubredditRepository
) : ViewModel() {
    private val TAG: String = "SubscribedViewModel"

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    //keeps track of current access token and if it changes,
    //we know we should update the current subscribed subreddit list to reflect on the data from the new access token
    private val _accessToken = MutableLiveData<String>()

//    val subreddits = _accessToken.switchMap {
//        redditApiRepository.getSubscribedSubredditsList().cachedIn(viewModelScope)
//    }


    fun checkIfAccessTokenChanged(accessToken: String) = viewModelScope.launch {
        //only if accessToken changes do we update subreddits
        if (_accessToken.value != accessToken) {
            _accessToken.value = accessToken
            getSubscribedSubredditsList()
        }
    }


    private fun getSubscribedSubredditsList() = viewModelScope.launch {
        val subscribedSubreddits = subscribedSubredditRepository.getSubscribedSubreddits()
        subscribedSubredditsFlow.value = subscribedSubreddits
    }

    private val subscribedSubredditsFlow = MutableStateFlow<List<SubscribedSubreddit>>(emptyList())
    val subscribedSubreddits: Flow<List<SubscribedSubreddit>> = subscribedSubredditsFlow


    init {
        getSubscribedSubredditsList()
    }









































//    private val subscribedSubreddits = mutableListOf<SubredditChildrenData>()
//
//    var after : String? = ""
//
//    fun recursion() = viewModelScope.launch{
//        after = ""
//        while(after != null) {
//            try {
//                test()
//            }catch (e: Exception) {
//
//            }
//
//        }
//        for (x in subscribedSubreddits) {
//            println("HELLO ${x.display_name_prefixed}")
//        }
//    }
//
//    suspend fun  test() {
//
//        val thing = redditApiRepository.getSubscribedSubredditsTest(after = after).data
//        if(thing != null) {
//            val mapped  = thing.children.map{it.data}
//
//
//            subscribedSubreddits.addAll(mapped)
//            after = thing.after
//
//
//        }else {
//            after = null
//        }
//
//
//
//
//    }



}