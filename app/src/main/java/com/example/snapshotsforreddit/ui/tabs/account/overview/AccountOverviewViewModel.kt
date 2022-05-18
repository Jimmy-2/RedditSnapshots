package com.example.snapshotsforreddit.ui.tabs.account.overview

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.AppTheme
import com.example.snapshotsforreddit.data.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.data.TestDataStoreRepository
import com.example.snapshotsforreddit.data.room.Account
import com.example.snapshotsforreddit.data.room.AccountDao
import com.example.snapshotsforreddit.di.ApplicationScope
import com.example.snapshotsforreddit.network.AuthApiRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import com.example.snapshotsforreddit.ui.tabs.settings.themeFromPreferences
import com.example.snapshotsforreddit.util.MonitorPair
import com.example.snapshotsforreddit.util.tryOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountOverviewViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
    private val redditApiRepository: RedditApiRepository,
    private val testDataStoreRepository: TestDataStoreRepository,
    private val preferencesDataStoreRepository: PreferencesDataStoreRepository,
    private val accountDao: AccountDao,
) : ViewModel() {
    private val TAG: String = "AccountOverviewViewModel"

    private val refreshSignal = MutableSharedFlow<Unit>()

    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    private val _navigationActions = Channel<AccountOverviewNavigationAction>(capacity = Channel.CONFLATED)

    val navigationActions = _navigationActions.receiveAsFlow()

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    //read and update isCompact value
    val preferencesFlow = preferencesDataStoreRepository.preferencesFlow.asLiveData()



    val username = MutableLiveData("")
    private val userData = MutableLiveData<UserInfo?>()
    private val isCompact = MutableLiveData<Boolean?>()

    val accountOverviewItems = Transformations.switchMap(MonitorPair(username,isCompact)) { pair ->
        redditApiRepository.getUserOverviewList(pair.first, 0, pair.second).cachedIn(viewModelScope)

    }

    fun checkIfUsernameChanged(username: String) {
        //only if accessToken changes do we update subreddits
        if (this.username.value != username) {
            //_username.value = username
            getLoggedInUserData(username)
        }
    }
    private fun getLoggedInUserData(username: String) = viewModelScope.launch {
        try {
            userData.value = redditApiRepository.getUserInfoData(username).data
            this@AccountOverviewViewModel.username.value = userData.value?.name
        } catch (e: Exception) {
            this@AccountOverviewViewModel.username.value = ""
        }
    }

    fun checkIsCompact(newVal : Boolean) {
        if(isCompact.value != newVal) {
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
                if(accountExist) {
                    updateAccount(refreshToken, it)
                }else {
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

    private fun updateAccount(newRefreshToken:String, username: String) = viewModelScope.launch {
        accountDao.updateLoggedIn(newRefreshToken, username)
    }


    private fun addAccountToApp(account: Account) = viewModelScope.launch {
        accountDao.insert(account)
    }

    fun onAccountsClicked() {
        _navigationActions.tryOffer(AccountOverviewNavigationAction.NavigateToAccountSelector)
    }
}

sealed class AccountOverviewNavigationAction {
    object NavigateToAccountSelector : AccountOverviewNavigationAction()
}
