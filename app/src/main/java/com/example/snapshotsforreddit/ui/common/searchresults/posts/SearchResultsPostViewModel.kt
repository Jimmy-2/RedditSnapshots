package com.example.snapshotsforreddit.ui.common.searchresults.posts

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.data.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.util.MonitorPair
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class CurrentSearch(val currentSearchQuery: String, val currentSubreddit: String?)

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val redditApiRepository: RedditApiRepository,
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository
) : ViewModel() {
    //read and update isCompact value
    val preferencesFlow = preferencesDataStoreRepository.preferencesFlow.asLiveData()

    private val currentSearch = MutableLiveData<CurrentSearch>()
    private val isCompact = MutableLiveData<Boolean>()

    val searchResults = Transformations.switchMap(MonitorPair(currentSearch, isCompact)) { pair ->
        redditApiRepository.getSearchResultsList(
            pair.first?.currentSubreddit,
            "r",
            pair.first?.currentSearchQuery,
            null,
            null,
            null,
            pair.second
        ).cachedIn(viewModelScope)
    }

    fun checkIsCompact(newVal: Boolean) {
        if (isCompact.value != newVal) {
            isCompact.value = newVal
        }
    }

    fun changeQuery(currentSearchQuery: CurrentSearch) {
        if (currentSearch.value != currentSearchQuery) {
            currentSearch.value = currentSearchQuery
        }

    }
}