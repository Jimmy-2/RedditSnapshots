package com.example.snapshotsforreddit.ui.common.otherusers.overview

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import com.example.snapshotsforreddit.util.tryOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserOverviewViewModel @Inject constructor(
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {
    private val refreshSignal = MutableSharedFlow<Unit>()

    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    private val _navigationActions =
        Channel<UserOverviewNavigationAction>(capacity = Channel.CONFLATED)

    val navigationActions = _navigationActions.receiveAsFlow()

    private val currentUser = MutableLiveData("")
    private val _userData = MutableLiveData<UserInfo?>()

    val userOverviewItems = currentUser.switchMap { userQuery ->
        redditApiRepository.getUserOverviewList(userQuery, 1, false).cachedIn(viewModelScope)
    }


    fun changeUserQuery(currentUserQuery: String) {

        if ((currentUser.value?.lowercase() ?: "") != currentUserQuery.lowercase()) {
            println("HELLO IS IT RELOADING? ${currentUser.value} ${currentUserQuery}")

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

    private val _infoType = MutableLiveData<Int>()
    val infoType: LiveData<Int> = _infoType

    private val _userInfo = MutableLiveData<RedditChildrenData>()
    val userInfo: LiveData<RedditChildrenData> = _userInfo

    fun onInfoClicked(infoItem: RedditChildrenData, type: Int) {
        _userInfo.value = infoItem
        _infoType.value = type
        _navigationActions.tryOffer(UserOverviewNavigationAction.NavigateToUserInfo)
    }


}

sealed class UserOverviewNavigationAction {
    object NavigateToUserInfo : UserOverviewNavigationAction()
}
