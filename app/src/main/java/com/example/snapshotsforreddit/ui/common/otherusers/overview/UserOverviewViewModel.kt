package com.example.snapshotsforreddit.ui.common.otherusers.overview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserOverviewViewModel @Inject constructor(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

    private val currentUser = MutableLiveData("")
    private val _userData = MutableLiveData<UserInfo?>()

    val userOverviewItems = currentUser.switchMap { userQuery ->
        redditApiRepository.getUserOverviewList(userQuery, 1, false).cachedIn(viewModelScope)
    }


    fun changeUserQuery(currentUserQuery: String) {
        if (currentUser.value != currentUserQuery) {
            //currentUser.value = currentUserQuery
            getSearchedUserData(currentUserQuery)
        }
    }

    private fun getSearchedUserData(username: String) = viewModelScope.launch {
        try {
            _userData.value = redditApiRepository.getUserInfoData(username).data
            currentUser.value = _userData.value?.name
        } catch (e: Exception) {

        }
    }


}