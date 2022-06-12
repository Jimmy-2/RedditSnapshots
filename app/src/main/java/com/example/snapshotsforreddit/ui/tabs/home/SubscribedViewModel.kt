package com.example.snapshotsforreddit.ui.tabs.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.datastore.AuthDataStoreRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData
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


    private val subscribedSubreddits = mutableListOf<SubredditChildrenData>()

    var after : String? = ""

    fun recursion() = viewModelScope.launch{
        after = ""
        while(after != null) {
            try {
                test()
            }catch (e: Exception) {

            }

        }
        for (x in subscribedSubreddits) {
            println("HELLO ${x.display_name_prefixed}")
        }
    }

    suspend fun  test() {

        val thing = redditApiRepository.getSubscribedSubredditsTest(after = after).data
        if(thing != null) {
            val mapped  = thing.children.map{it.data}


            subscribedSubreddits.addAll(mapped)
            after = thing.after


        }else {
            after = null
        }




    }



}