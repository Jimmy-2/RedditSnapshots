package com.example.snapshotsforreddit.ui.post

import android.content.ClipData
import androidx.lifecycle.*
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.database.Post
import com.example.snapshotsforreddit.database.PostDao
import com.example.snapshotsforreddit.network.responses.ChildrenData
import kotlinx.coroutines.launch

//allow user to save a post to database
//dao
class PostDetailViewModel(private val postDao: PostDao) : ViewModel() {

    //when a user saves or upvotes a post, post to the api. and then get the result back from json.
    //do not refresh entire post


    private val _postLink = MutableLiveData<String>()
    val postLink: LiveData<String> = _postLink


    fun retrievePostLink(link: String?) {
        _postLink.value = link!!
    }



    //add new post to database
    fun addNewPost() {
        val newPost = getNewPostEntry("test title", "Testing.com")
        insertPost(newPost)

    }

    //Launching a new coroutine to add post to database in an asynchronous way
    private fun insertPost(post: Post) {
        viewModelScope.launch {
            postDao.insert(post)
        }
    }


    private fun getNewPostEntry(title: String, link: String): Post {
        return Post(
            title = title,
            permalink = link
        )
    }






    init {
        //get current post link from adapter
    }











}
class PostDetailViewModelFactory(private val postDao: PostDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostDetailViewModel(postDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}