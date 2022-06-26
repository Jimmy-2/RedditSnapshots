package com.example.snapshotsforreddit.ui.common.redditpage

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePost
import com.example.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePostRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.util.MonitorTriple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedditPageViewModel @Inject constructor(
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    private val redditApiRepository: RedditApiRepository,
    private val redditPagePostRepository: RedditPagePostRepository
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


    private val isCompactTest2 = MutableStateFlow<Boolean?>(null)

    private val isCompactTest : MutableLiveData<Boolean> = MutableLiveData(true)
    private val sortOrderTest2 : MutableLiveData<String>  = MutableLiveData("Default value")
    val subredditNameTest2: MutableLiveData<String> = MutableLiveData("Default value")


    val redditPagePostsTest = combine(_subredditName.asFlow(),sortOrder.asFlow(), isCompact.asFlow())
    { redditPageName, sortOrder, isCompact ->
        Triple(redditPageName, sortOrder, isCompact)
    }.flatMapLatest { (redditPageName, sortOrder, isCompact) ->
        //only executed if redditpage name is not null
        redditPageName?.let {
            redditPagePostRepository.getRedditPostsPaged("popular", sortOrder ?: "best")
        }?: emptyFlow()

    }.cachedIn(viewModelScope)

    private val currentQuery = MutableStateFlow<String?>(null)

    val redditPagePostsTest2 = currentQuery.flatMapLatest { query ->
        //only executed if redditpage name is not null
        query ?.let {
            println("HELLO WORK PLEASE")
            redditPagePostRepository.getRedditPostsPaged(query, sortOrder.value ?: "best")
        }?: emptyFlow()

    }.cachedIn(viewModelScope)



    fun onSearchQuerySubmit(query: String) {
        currentQuery.value = query
    }

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

    //caching

    fun updatePostAfterClick(updatedRedditPost: RedditPagePost) = viewModelScope.launch {
        redditPagePostRepository.updateRedditPagePage(updatedRedditPost)
    }

    fun onVoteClick(redditPost: RedditPagePost, voteType: Int) {
        val currentVoteState = redditPost.likes

        //0 for upvote clicked, 1 for downvote clicked
        when (voteType) {
            0 -> {
                if (currentVoteState == true) {
                    val updatedRedditPost = redditPost.copy(likes = null)
                    updatePostAfterClick(updatedRedditPost)
                    //TODO THEN POST TO REDDIT API SERVERS
                    //onClickListener.onVoteClick(post!!, 0)
                } else {
                    val updatedRedditPost = redditPost.copy(likes = true)
                    updatePostAfterClick(updatedRedditPost)
                    //onClickListener.onVoteClick(post!!, 1)
                }
            }
            1 -> {
                if (currentVoteState == false) {
                    val updatedRedditPost = redditPost.copy(likes = null)
                    updatePostAfterClick(updatedRedditPost)
                    //onClickListener.onVoteClick(post!!, 0)
                } else {
                    val updatedRedditPost = redditPost.copy(likes = false)
                    updatePostAfterClick(updatedRedditPost)
                    //onClickListener.onVoteClick(post!!, -1)
                }
            }
        }

    }





    fun onSaveClick(redditPost: RedditPagePost) {

    }





}
