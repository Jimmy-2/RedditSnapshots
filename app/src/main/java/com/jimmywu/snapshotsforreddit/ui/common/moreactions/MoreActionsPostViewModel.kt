package com.jimmywu.snapshotsforreddit.ui.common.moreactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePost
import com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePostRepository
import com.jimmywu.snapshotsforreddit.network.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreActionsPostViewModel @Inject constructor(
    private val redditApiRepository: RedditApiRepository,
    private val redditPagePostRepository: RedditPagePostRepository,
) : ViewModel() {

    val actionCompleted = MutableStateFlow<Boolean>(false)


    private fun updatePostAfterVote(voteVal: Int, updatedRedditPost: RedditPagePost, ) = viewModelScope.launch {
        try {
            updatedRedditPost.name.let {
                println("HELLO VOTING")
                redditApiRepository.voteOnThing(voteVal, it)
            }
            redditPagePostRepository.updateRedditPagePost(updatedRedditPost)
            actionCompleted.value = true
        }
        catch (e: Exception) {
            //TODO show exception to user
            //Some posts can no longer be voted on due to age. let the user know
        }
    }


    private fun updatePostAfterSave(newSaveStatus: Boolean, updatedRedditPost: RedditPagePost) =
        viewModelScope.launch {
            //update in database

            try {
                //update in reddit servers
                updatedRedditPost.name.let {
                    if (newSaveStatus) {

                        println("HELLO REACHED SAVE")
                        redditApiRepository.saveItem(it)


                    } else {
                        println("HELLO REACHED UNSAVE")
                        redditApiRepository.unsaveItem(it)

                    }
                }

                redditPagePostRepository.updateRedditPagePost(updatedRedditPost)
                actionCompleted.value = true
            } catch (e: Exception) {
                //TODO show exception to user
            }

        }


    suspend fun onUpvoteClicked(postName: String, postRedditPageAndSort: String, currentVoteStatus: String) {
        val currentRedditPagePost = redditPagePostRepository.getRedditPagePost(postName, postRedditPageAndSort)
        when (currentVoteStatus) {
            "true" -> {
                val updatedRedditPost = currentRedditPagePost.copy(likes = null)
                updatePostAfterVote(0, updatedRedditPost)

            }
            else -> {

                val updatedRedditPost = currentRedditPagePost.copy(likes = true)
                updatePostAfterVote(1,updatedRedditPost)
                //onClickListener.onVoteClick(post!!, -1)

            }

        }
    }


    suspend fun onDownvoteClicked(postName: String, postRedditPageAndSort: String, currentVoteStatus: String) {
        val currentRedditPagePost = redditPagePostRepository.getRedditPagePost(postName, postRedditPageAndSort)
        when (currentVoteStatus) {
            "false" -> {
                val updatedRedditPost = currentRedditPagePost.copy(likes = null)
                updatePostAfterVote(0, updatedRedditPost)

            }
            else -> {

                val updatedRedditPost = currentRedditPagePost.copy(likes = false)
                updatePostAfterVote(-1,updatedRedditPost)
            }

        }
    }



    // primaryKeys = ["name", "redditPageNameAndSortOrder"]
    suspend fun onSaveClicked(postName: String, postRedditPageAndSort: String) {
//        val currentRedditPagePost: RedditPagePost = redditPagePostDao.getRedditPagePost(postName, postRedditPageAndSort).first()
        val currentRedditPagePost = redditPagePostRepository.getRedditPagePost(postName, postRedditPageAndSort)

        if (currentRedditPagePost.saved != null) {
            val newSaveStatus = !currentRedditPagePost.saved
            val updatedRedditPost = currentRedditPagePost.copy(saved = newSaveStatus)
            updatePostAfterSave(newSaveStatus, updatedRedditPost)
        }
    }




}


