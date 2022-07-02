package com.jimmywu.snapshotsforreddit.ui.tabs.snapshots

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jimmywu.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.jimmywu.snapshotsforreddit.data.datastore.SortOrder
import com.jimmywu.snapshotsforreddit.data.room.snapshots.Snapshot
import com.jimmywu.snapshotsforreddit.data.room.snapshots.SnapshotDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SnapshotsViewModel @Inject constructor(
    private val snapshotDao: SnapshotDao,
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository
): ViewModel() {
    //holds a single value like mutable live data which holds a string of values
    val searchQuery = MutableStateFlow("")

    //val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    //val isCompactView = MutableStateFlow(false)
    val preferencesFlow = preferencesDataStoreRepository.preferencesFlow

    //use combine to query by multiple flows.
    //private val postsFlow = searchQuery.flatMapLatest {
    private val postsFlow = combine(searchQuery, preferencesFlow) {
        query, filterPreferences -> Pair(query, filterPreferences)
    }.flatMapLatest {
        //whenever searchQuery or sortOrder value changes, we execute the following
        snapshotDao.getSnapshots(it.first, it.second.sortOrder) //it is the searchQuery and sortOrder combine value
        //once we run postDao.getDownloadedPosts on the searchQuery and sortOrder, we assign the returned value to postsFlow
    }



    //automatically get new data from this flow if any changes occur to database
    //val downloadedPosts: LiveData<List<Post>> = postDao.getDownloadedPosts().asLiveData()
    val downloadedPosts: LiveData<List<Snapshot>> = postsFlow.asLiveData()

    //with suspend functions, we need coroutines to run them
    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        //update the sort order value in datastore on sort button clicked
        preferencesDataStoreRepository.updateSortOrder(sortOrder)
    }

    fun onCompactViewClicked(isCompactView: Boolean) = viewModelScope.launch{
        //update iscompactview in datastore on compact button clicked
        preferencesDataStoreRepository.updateIsCompactView(isCompactView)
    }

    fun onPostSelected(snapshot: Snapshot) {

    }

    fun onPostSwiped(snapshot: Snapshot) = viewModelScope.launch {
        snapshotDao.delete(snapshot)
    }


}

