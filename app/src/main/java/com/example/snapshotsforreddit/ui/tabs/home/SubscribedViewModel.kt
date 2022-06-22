package com.example.snapshotsforreddit.ui.tabs.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.snapshotsforreddit.data.datastore.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.room.cache.subscribedsubreddit.SubscribedSubredditRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribedViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val redditApiRepository: RedditApiRepository,
    private val subscribedSubredditRepository: SubscribedSubredditRepository
) : ViewModel() {
    private val TAG: String = "SubscribedViewModel"

    private val refreshTriggerSignal = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerSignal.receiveAsFlow()

    val authFlow = authDataStoreRepository.authFlow.asLiveData()


    //keeps track of current access token and if it changes, (due to expiration/account switch/logoff)
    //we know we should update the current subscribed subreddit list to reflect on the data from the new access token
    private val _accessToken = MutableLiveData<String>()

//    val subreddits = _accessToken.switchMap {
//        redditApiRepository.getSubscribedSubredditsList().cachedIn(viewModelScope)
//    }

    var scrollToTop = MutableLiveData<Boolean>()

    //every time we send a value to trigger the refreshTrigger,
    //flatmaplatest and the code inside will be called and the subscribedSubreddits flow will switch to the new flow from getSubscribedSubreddits()
    val subscribedSubreddits = refreshTrigger.flatMapLatest {
        subscribedSubredditRepository.getSubscribedSubreddits(
            forceRefresh = true
        )

    }.stateIn(viewModelScope, SharingStarted.Lazily, null)


    fun checkIfAccessTokenChanged(accessToken: String) {
        //only if accessToken changes do we update subreddits
        if (_accessToken.value != accessToken) {
            _accessToken.value = accessToken
            onRefresh()
        }
    }


    fun onRefresh() {
        //do not want to reload if we are already loading data from an ongoing reload

        if (subscribedSubreddits.value !is Resource.Loading) {
            viewModelScope.launch {
//                    scrollToTop = true
                refreshTriggerSignal.send(Refresh.FORCE)
            }
        }


    }


    enum class Refresh {
        FORCE, NORMAL
    }

    sealed class Event {
        data class ShowErrorMessage(val error: Throwable) : Event()
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