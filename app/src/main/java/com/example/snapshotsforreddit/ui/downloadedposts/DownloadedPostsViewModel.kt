package com.example.snapshotsforreddit.ui.downloadedposts

import androidx.lifecycle.*
import com.example.snapshotsforreddit.data.room.Post
import com.example.snapshotsforreddit.data.room.PostDao
import kotlinx.coroutines.launch

class DownloadedPostsViewModel(private val postDao: PostDao) : ViewModel() {
    //val allItems: LiveData<List<Post>> = postDao.getDownloadedPosts().asLiveData()

    //using coroutines to delete an entry from database asynchronously
    fun deleteItem(item: Post) {
        viewModelScope.launch {
            postDao.delete(item)
        }
    }


}
class DownloadedPostsViewModelFactory(private val postDao: PostDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DownloadedPostsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DownloadedPostsViewModel(postDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}