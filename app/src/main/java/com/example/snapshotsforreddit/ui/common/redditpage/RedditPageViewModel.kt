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

    private val sortOrderTest2: MutableLiveData<String> = MutableLiveData("Default value")
    val subredditNameTest2: MutableLiveData<String> = MutableLiveData("Default value")


//    val redditPagePostsTest = combine(_subredditName.asFlow(),sortOrder.asFlow(), isCompact.asFlow())
//    { redditPageName, sortOrder, isCompact ->
//        Triple(redditPageName, sortOrder, isCompact)
//    }.flatMapLatest { (redditPageName, sortOrder, isCompact) ->
//        //only executed if redditpage name is not null
//        redditPageName?.let {
//            redditPagePostRepository.getRedditPostsPaged("popular", sortOrder ?: "best")
//        }?: emptyFlow()
//
//    }.cachedIn(viewModelScope)



    private val currentRedditPageName = MutableStateFlow<String?>(null)
    private val sortOrderTest = MutableStateFlow<String?>(null)
    private val isCompactTest = MutableStateFlow<Boolean?>(null)


//    val redditPagePostsTest2  = currentRedditPageName.flatMapLatest { redditPageName ->
//        redditPageName?.let {
//            redditPagePostRepository.getRedditPostsPaged(
//                redditPageName,
//                "best",
//                true
//            )
//        } ?: emptyFlow()
//    }.cachedIn(viewModelScope)

    val redditPagePostsTest2  = currentRedditPageName.flatMapLatest { redditPageName ->
        redditPageName?.let {
            redditPagePostRepository.getRedditPostsPaged(
                redditPageName,
                "best",
                true
            )
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    fun onRedditPageLoad(redditPageName: String) {
        println("HELLO123 HEY PAGE LOADING")
        if(currentRedditPageName.value != redditPageName) {
            currentRedditPageName.value = redditPageName
            newQueryInProgress = true
            pendingScrollToTopAfterNewQuery = true
        }


    }

    var refreshInProgress = false
    var pendingScrollToTopAfterRefresh = false

    var newQueryInProgress = false
    var pendingScrollToTopAfterNewQuery = false




//
//    val redditPagePostsTest2 = combine(currentRedditPageName, sortOrderTest, isCompactTest)
//    { redditPageName, sortOrder, isCompact ->
//        Triple(redditPageName, sortOrder, isCompact)
//    }.flatMapLatest { (redditPageName, sortOrder, isCompact) ->
//        //only executed if redditpage name is not null
//        redditPageName?.let {
//            redditPagePostRepository.getRedditPostsPaged(
//                redditPageName,
//                sortOrder ?: "best",
//                isCompact ?: false
//            )
//        } ?: emptyFlow()
//
//
//
//    }.cachedIn(viewModelScope)

//


//    val redditPagePostsTest2  =
//        redditPagePostRepository.getRedditPostsPaged(
//                "ligma",
//                "best",
//                true
//            ).cachedIn(viewModelScope)



//    val redditPagePostsTest3 = currentRedditPageName.combine(isCompact.asFlow()){ redditPageName, isCompact ->
//        Pair(redditPageName, isCompact )
//    }.flatMapLatest { (redditPageName, isCompact) ->
//        //only executed if redditpage name is not null
//        redditPageName ?.let {
//            redditPagePostRepository.getRedditPostsPaged(redditPageName, sortOrder.value ?: "best", isCompact = isCompact)
//        }?: emptyFlow()
//
//    }.cachedIn(viewModelScope)





    val redditPagePosts =
        Transformations.switchMap(MonitorTriple(isCompact, sortOrder, _subredditName)) {
            redditApiRepository.getSubredditPostsList(
                it.third!!, subredditType.value!!, isDefault.value,
                it.second, it.first
            ).cachedIn(viewModelScope)


        }


    fun onVoteOnPost(typeOfVote: Int, post: RedditChildrenData) = viewModelScope.launch {
        try {
            post.name?.let { redditApiRepository.voteOnThing(typeOfVote, it) }
        } catch (e: Exception) {
        }
    }

    fun onSortOrderSelected(newSortOrder: String) {
        if (sortOrder.value != newSortOrder) {
            sortOrder.value = newSortOrder
        }
    }

    fun onCompactViewClicked(isCompactView: Boolean) = viewModelScope.launch {
        //update isCompactView in datastore on compact button clicked
        preferencesDataStoreRepository.updateIsCompactView(isCompactView)
        checkIsCompact(isCompactView)
    }

    fun checkIsCompact(newVal: Boolean) {
        if (isCompact.value != newVal) {
            isCompact.value = newVal
            isCompactTest.value = newVal
        }
    }

    fun loadRedditPage(redditPageName: String, redditPageType: String, default: Boolean) {
        if (_subredditName.value == null || _subredditName.value == "") {
            _subredditName.value = redditPageName
            subredditType.value = redditPageType
            isDefault.value = default
        }
    }

    fun changeRedditPage(redditPageName: String, redditPageType: String) {
        isDefault.value = false
        if (_subredditName.value != redditPageName) {
            _subredditName.value = redditPageName
        }
        if (subredditType.value != redditPageType) {
            subredditType.value = redditPageType
        }

    }

    //caching


    private fun updatePostAfterClick(voteVal: Int, updatedRedditPost: RedditPagePost, ) = viewModelScope.launch {
        redditPagePostRepository.updateRedditPagePage(updatedRedditPost)
        try {
            updatedRedditPost.name.let { redditApiRepository.voteOnThing(voteVal, it) }
        }
        catch (e: Exception) {
            //TODO show exception to user
        }
    }

    fun onVoteClick(redditPost: RedditPagePost, isUpvote: Boolean?) {
        //0 for upvote clicked, 1 for downvote clicked
        when (isUpvote) {
            true -> {

                val updatedRedditPost = redditPost.copy(likes = true)
                updatePostAfterClick(1, updatedRedditPost)

            }
            false -> {

                val updatedRedditPost = redditPost.copy(likes = false)
                updatePostAfterClick(-1,updatedRedditPost)
                //onClickListener.onVoteClick(post!!, -1)

            }
            else -> {
                val updatedRedditPost = redditPost.copy(likes = null)
                updatePostAfterClick(0,updatedRedditPost)
            }
        }

    }


    fun onSaveClick(redditPost: RedditPagePost) {

    }


}
