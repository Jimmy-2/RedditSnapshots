package com.example.snapshotsforreddit.ui.post

import android.content.ClipData
import androidx.lifecycle.*
import com.example.snapshotsforreddit.database.Post
import com.example.snapshotsforreddit.database.PostDao
import com.example.snapshotsforreddit.network.responses.ChildrenData
import kotlinx.coroutines.launch

//allow user to save a post to database
//dao
class PostDetailViewModel(private val postDao: PostDao) : ViewModel() {

    //pass the link from the list adapter
    private val _postLink = MutableLiveData<List<String>>()
    val postLink: LiveData<List<String>> = _postLink

    val allItems: LiveData<List<Post>> = postDao.getSavedPosts().asLiveData()

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

    fun addNewPost() {
        val newPost = getNewPostEntry("test title", "Testing.com")
        insertPost(newPost)
        println("HELLO")
    }




    init {
        //get current post link from adapter
    }

    val hello = "HELLO"



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