package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.account.overview

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.jimmywu.snapshotsforreddit.BuildConfig
import com.jimmywu.snapshotsforreddit.data.datastore.AuthDataStoreRepository
import com.jimmywu.snapshotsforreddit.data.datastore.PreferencesDataStoreRepository
import com.jimmywu.snapshotsforreddit.data.room.loggedinaccounts.Account
import com.jimmywu.snapshotsforreddit.data.room.loggedinaccounts.AccountDao
import com.jimmywu.snapshotsforreddit.network.AuthApiRepository
import com.jimmywu.snapshotsforreddit.network.RedditApiRepository
import com.jimmywu.snapshotsforreddit.network.responses.RedditChildrenData
import com.jimmywu.snapshotsforreddit.network.responses.TokenResponse
import com.jimmywu.snapshotsforreddit.network.responses.account.UserInfo
import com.jimmywu.snapshotsforreddit.util.MonitorPair
import com.jimmywu.snapshotsforreddit.util.tryOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountOverviewViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
    private val redditApiRepository: RedditApiRepository,
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    private val accountDao: AccountDao,
) : ViewModel() {
    private val TAG: String = "AccountOverviewViewModel"

    private val refreshSignal = MutableSharedFlow<Unit>()

    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    private val _navigationActions =
        Channel<AccountOverviewNavigationAction>(capacity = Channel.CONFLATED)

    val navigationActions = _navigationActions.receiveAsFlow()

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    //read and update isCompact value
//    val preferencesFlow = preferencesDataStoreRepository.preferencesFlow.asLiveData()

    val preferencesFlow = preferencesDataStoreRepository.preferencesFlow

    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val userData = MutableLiveData<UserInfo?>()
    private val isCompact = MutableLiveData<Boolean?>()
    val _isCompact: LiveData<Boolean?> = isCompact

    val accountOverviewItems = Transformations.switchMap(MonitorPair(_username, isCompact)) { pair ->
        redditApiRepository.getUserOverviewList(pair.first, 0, pair.second).cachedIn(viewModelScope)

    }

    fun checkIfUsernameChanged(username: String) {
        //only if accessToken changes do we update subreddits
        if (this._username.value != username) {
            //_username.value = username
            getLoggedInUserData(username)
        }
    }

    private fun getLoggedInUserData(username: String) = viewModelScope.launch {
        try {
            userData.value = redditApiRepository.getUserInfoData(username).data
            this@AccountOverviewViewModel._username.value = userData.value?.name
        } catch (e: Exception) {
            this@AccountOverviewViewModel._username.value = ""
        }
    }

    fun checkIsCompact(newVal: Boolean) {
        if (isCompact.value != newVal) {
            isCompact.value = newVal
        }
    }

    fun checkCode(uri: Uri?) {
        if (uri!!.getQueryParameter("error") != null) {
            val error = uri.getQueryParameter("error")
            Log.e(ContentValues.TAG, "An error has occurred : $error")
        } else {

            val currState = uri.getQueryParameter("state")
            val currCode = uri.getQueryParameter("code")
            if (currState == BuildConfig.AUTH_STATE && currCode != null) {
                getTokens(currCode)

            } else {
                //TODO: Display error to user

            }
        }

    }

    private fun getTokens(code: String) = viewModelScope.launch {
        try {
            onTokensUpdated(authApiRepository.getTokenValues(code))
        } catch (e: Exception) {

        }
    }

    private fun onTokensUpdated(tokenResponse: TokenResponse) = viewModelScope.launch {
        //update the sort order value in datastore on sort button clicked
        val accessToken = tokenResponse.access_token
        val refreshToken = tokenResponse.refresh_token
        if (accessToken != null && refreshToken != null) {
            authDataStoreRepository.updateAccessToken(accessToken)
            authDataStoreRepository.updateRefreshToken(refreshToken)
            authDataStoreRepository.updateLoginState(true)
            redditApiRepository.getUsername().name?.let {
                //CHECK IF USER EXISTS THEN ADD ACCOUNT OTHERWISE UPDATE
                val accountExist = accountDao.exists(it)
                if (accountExist) {
                    updateAccount(refreshToken, it)
                } else {
                    val newLoggedInAccount = Account(
                        username = it,
                        refreshToken = refreshToken,
                        accessToken = accessToken
                    )
                    addAccountToApp(newLoggedInAccount)
                }
                authDataStoreRepository.updateUsername(
                    it
                )
            }
        } else {
            Log.v(TAG, "Log in fail")
        }
    }

    private fun updateAccount(newRefreshToken: String, username: String) = viewModelScope.launch {
        accountDao.updateLoggedIn(newRefreshToken, username)
    }

    private fun addAccountToApp(account: Account) = viewModelScope.launch {
        accountDao.insert(account)
    }

    fun onAccountsClicked() {
        _navigationActions.tryOffer(AccountOverviewNavigationAction.NavigateToAccountSelector)
    }

    private val _infoType = MutableLiveData<Int>()
    val infoType: LiveData<Int> = _infoType

    private val _userInfo = MutableLiveData<RedditChildrenData>()
    val userInfo: LiveData<RedditChildrenData> = _userInfo

    fun onInfoClicked(infoItem: RedditChildrenData, type: Int) {
        _userInfo.value = infoItem
        _infoType.value = type
        _navigationActions.tryOffer(AccountOverviewNavigationAction.NavigateToUserInfo)
    }
}

sealed class AccountOverviewNavigationAction {
    object NavigateToAccountSelector : AccountOverviewNavigationAction()
    object NavigateToUserInfo : AccountOverviewNavigationAction()
}
