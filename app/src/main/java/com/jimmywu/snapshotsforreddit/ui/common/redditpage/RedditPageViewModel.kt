package com.jimmywu.snapshotsforreddit.ui.common.redditpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jimmywu.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePost
import com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePostRepository
import com.jimmywu.snapshotsforreddit.network.RedditApiRepository
import com.jimmywu.snapshotsforreddit.network.responses.RedditChildrenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedditPageViewModel @Inject constructor(
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    private val redditApiRepository: RedditApiRepository,
    private val redditPagePostRepository: RedditPagePostRepository
) : ViewModel() {

    //read and update isCompact value from data store
    val preferencesFlow = preferencesDataStoreRepository.preferencesFlow

    private val _subredditName = MutableLiveData<String>()
    val subredditName: LiveData<String> = _subredditName

    private val subredditType = MutableLiveData<String>()
    private val isCompact = MutableLiveData<Boolean?>()
    private val sortOrder = MutableLiveData<String?>()
    private val isDefault = MutableLiveData<Boolean?>()


    private val sortOrderTest2: MutableLiveData<String> = MutableLiveData("Default value")
    val subredditNameTest2: MutableLiveData<String> = MutableLiveData("Default value")

    private val currentRedditPageName = MutableStateFlow<String?>(null)
    val _currentRedditPageName: StateFlow<String?> = currentRedditPageName
    private val sortOrderTest = MutableStateFlow<String?>("best")
    private val isCompactTest = MutableStateFlow<Boolean?>(false)


    //no longer updating and refreshing the entire paged list when switching views(compact and non compact).
    //now the view/layout changes happen dynamically
    val redditPagePosts: Flow<PagingData<RedditPagePost>> =
        currentRedditPageName.combine(sortOrderTest) { redditPageName, sortOrder ->
            Pair(redditPageName, sortOrder)
        }.flatMapLatest { (redditPageName, sortOrder) ->
            //only executed if redditPageName is not null, otherwise returns an empty flow
            redditPageName?.let {
                redditPagePostRepository.getRedditPostsPaged(
                    it,
                    sortOrder ?: "best",)
            } ?: emptyFlow()

            //add logic to not get new data from api when changing compact views.
        }.cachedIn(viewModelScope)



//    val redditPagePostsNotDynamic: Flow<PagingData<RedditPagePost>> =
//        currentRedditPageName.combine(sortOrderTest) { redditPageName, sortOrder ->
//            Pair(redditPageName, sortOrder)
//        }.combine(isCompactTest) { nameAndSortPair, isCompact ->
//            Pair(nameAndSortPair, isCompact)
//        }.flatMapLatest { (nameAndSortPair, isCompact) ->
//            //only executed if redditPageName is not null, otherwise returns an empty flow
//            nameAndSortPair.first?.let {
//                redditPagePostRepository.getRedditPostsPaged(
//                    it,
//                    nameAndSortPair.second ?: "best",
//                    isCompact ?: false
//                )
//            } ?: emptyFlow()
//
//            //add logic to not get new data from api when changing compact views.
//        }.cachedIn(viewModelScope)
//


//    val redditPagePostsTest2 = currentRedditPageName.flatMapLatest { redditPageName ->
//        redditPageName?.let {
//            redditPagePostRepository.getRedditPostsPaged(
//                redditPageName,
//                "best",
//                true
//            )
//        } ?: emptyFlow()
//    }.cachedIn(viewModelScope)


    var refreshInProgress = false
    var pendingScrollToTopAfterRefresh = false

    var newSubredditInProgress = false
    var pendingScrollToTopAfterNewSubreddit = false

    var refreshingViewsOnCompactChange = false

    fun onRedditPageLoad(redditPageName: String, default: Boolean) {
        if (currentRedditPageName.value == null || currentRedditPageName.value == "") {
            currentRedditPageName.value = ""
            currentRedditPageName.value = redditPageName
            _subredditName.value = redditPageName
        }
//        if(currentRedditPageName.value != redditPageName) {
//            currentRedditPageName.value = ""
//            currentRedditPageName.value = redditPageName
//            _subredditName.value = redditPageName
//
//        }


    }

    fun onSortOrderSelected(newSortOrder: String) {
        if (sortOrderTest.value != newSortOrder) {
            sortOrderTest.value = newSortOrder
        }
    }



    fun onVoteOnPost(typeOfVote: Int, post: RedditChildrenData) = viewModelScope.launch {
        try {
            post.name?.let { redditApiRepository.voteOnThing(typeOfVote, it) }
        } catch (e: Exception) {
        }
    }

    fun onCompactViewClicked(isCompact: Boolean) = viewModelScope.launch {
        //update isCompactView in datastore on compact button clicked
        preferencesDataStoreRepository.updateIsCompactView(isCompact)

//        checkIsCompact(isCompactView)

        redditPagePostRepository.updateCompactView(isCompact)
    }

    fun checkIsCompact(newVal: Boolean) {
        if (isCompactTest.value != newVal) {
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

        if (currentRedditPageName.value != redditPageName) {
            currentRedditPageName.value = redditPageName
        }
//        if (subredditType.value != redditPageType) {
//            subredditType.value = redditPageType
//        }

    }

    //caching


    private fun updatePostAfterClick(voteVal: Int, updatedRedditPost: RedditPagePost) =
        viewModelScope.launch {
            try {
                updatedRedditPost.name.let { redditApiRepository.voteOnThing(voteVal, it) }
                redditPagePostRepository.updateRedditPagePost(updatedRedditPost)
            } catch (e: Exception) {
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
                updatePostAfterClick(-1, updatedRedditPost)
                //onClickListener.onVoteClick(post!!, -1)

            }
            else -> {
                val updatedRedditPost = redditPost.copy(likes = null)
                updatePostAfterClick(0, updatedRedditPost)
            }
        }
    }

    fun onSaveClick(redditPost: RedditPagePost) {

    }


}
