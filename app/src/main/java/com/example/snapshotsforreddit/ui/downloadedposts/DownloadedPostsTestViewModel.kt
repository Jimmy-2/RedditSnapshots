package com.example.snapshotsforreddit.ui.downloadedposts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.snapshotsforreddit.data.PreferencesRepository
import com.example.snapshotsforreddit.data.SortOrder
import com.example.snapshotsforreddit.data.room.Post
import com.example.snapshotsforreddit.data.room.PostDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadedPostsTestViewModel @Inject constructor(
    private val postDao: PostDao,
    private val preferencesRepository: PreferencesRepository
): ViewModel() {
    //holds a single value like mutable live data which holds a string of values
    val searchQuery = MutableStateFlow("")

    //val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    //val isCompactView = MutableStateFlow(false)
    val preferencesFlow = preferencesRepository.preferencesFlow

    //use combine to query by multiple flows.
    //private val postsFlow = searchQuery.flatMapLatest {
    private val postsFlow = combine(searchQuery, preferencesFlow) {
        query, filterPreferences -> Pair(query, filterPreferences)
    }.flatMapLatest {
        //whenever searchQuery or sortOrder value changes, we execute the following
        postDao.getDownloadedPosts(it.first, it.second.sortOrder) //it is the searchQuery and sortOrder combine value
        //once we run postDao.getDownloadedPosts on the searchQuery and sortOrder, we assign the returned value to postsFlow
    }

    //with suspend functions, we need coroutines to run them
    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        //update the sort order value in datastore on sort button clicked
        preferencesRepository.updateSortOrder(sortOrder)
    }

    fun onCompactViewClicked(isCompactView: Boolean) = viewModelScope.launch{
        //update iscompactview in datastore on compact button clicked
        preferencesRepository.updateIsCompactView(isCompactView)
    }

    //automatically get new data from this flow if any changes occur to database
    //val downloadedPosts: LiveData<List<Post>> = postDao.getDownloadedPosts().asLiveData()
    val downloadedPosts: LiveData<List<Post>> = postsFlow.asLiveData()
}

