package com.example.snapshotsforreddit.ui.downloadedposts

import androidx.lifecycle.*
import com.example.snapshotsforreddit.database.Post
import com.example.snapshotsforreddit.database.PostDao
import kotlinx.coroutines.launch

class SavedPostsViewModel(private val postDao: PostDao) : ViewModel() {
    val allItems: LiveData<List<Post>> = postDao.getSavedPosts().asLiveData()

    //using coroutines to delete an entry from database asynchronously
    fun deleteItem(item: Post) {
        viewModelScope.launch {
            postDao.delete(item)
        }
    }


}
class SavedPostsViewModelFactory(private val postDao: PostDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedPostsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SavedPostsViewModel(postDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}