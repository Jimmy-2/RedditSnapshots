package com.example.snapshotsforreddit.ui.tabs.inbox

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.network.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    private val currentInboxType = MutableLiveData<String>()

    fun loadInbox(inboxType: String) {
        if(currentInboxType.value != inboxType) {
            currentInboxType.value = inboxType
        }
    }

    val inboxItems = currentInboxType.switchMap {
        redditApiRepository.getInboxList(it).cachedIn(viewModelScope)
    }






}