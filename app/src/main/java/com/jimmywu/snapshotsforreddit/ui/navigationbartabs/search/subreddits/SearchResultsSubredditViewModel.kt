package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.search.subreddits

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.jimmywu.snapshotsforreddit.network.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SearchResultsSubredditViewModel @Inject constructor(private val redditApiRepository: RedditApiRepository): ViewModel() {
    private val currentSearch = MutableLiveData<String>()

    val searchResults = currentSearch.switchMap { search ->
        redditApiRepository.getSearchResultsSubreddit(
            search,
            "sr",
            null,
        ).cachedIn(viewModelScope)

    }

    fun changeQuery(currentSearchQuery: String) {
        if (currentSearch.value != currentSearchQuery) {
            currentSearch.value = currentSearchQuery
        }

    }
}