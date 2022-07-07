package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.search

import androidx.lifecycle.ViewModel
import com.jimmywu.snapshotsforreddit.network.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val redditApiRepository: RedditApiRepository) :ViewModel() {

    val searchQuery = MutableStateFlow("")

//    private val _searchQuery = MutableLiveData<String>()
//    val searchQuery : LiveData<String> = _searchQuery
//
//    val searchResults =
}