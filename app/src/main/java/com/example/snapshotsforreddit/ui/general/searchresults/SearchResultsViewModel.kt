package com.example.snapshotsforreddit.ui.general.searchresults

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.network.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class CurrentSearch(val currentSearchQuery: String, val currentSubreddit: String?)

@HiltViewModel
class SearchResultsViewModel @Inject constructor(private val redditApiRepository: RedditApiRepository) : ViewModel() {
    private val currentSearch = MutableLiveData<CurrentSearch>()

    val searchResults = currentSearch.switchMap { search ->
        redditApiRepository.getSearchResultsList(
            search.currentSubreddit,
            "r",
            search.currentSearchQuery,
            null,
            null,
            null
        ).cachedIn(viewModelScope)
    }

    fun changeQuery(currentSearchQuery: CurrentSearch) {
        if (currentSearch.value != currentSearchQuery) {
            currentSearch.value = currentSearchQuery
        }

    }
}