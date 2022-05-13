package com.example.snapshotsforreddit.ui.tabs.account.overview

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.PreferencesDataStoreRepository
import com.example.snapshotsforreddit.data.room.Account
import com.example.snapshotsforreddit.data.room.AccountDao
import com.example.snapshotsforreddit.network.AuthApiRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import com.example.snapshotsforreddit.util.MonitorPair
import com.example.snapshotsforreddit.util.tryOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _navigationActions = Channel<AccountOverviewNavigationAction>(capacity = Channel.CONFLATED)

    val navigationActions = _navigationActions.receiveAsFlow()

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    //read and update isCompact value
    val preferencesFlow = preferencesDataStoreRepository.preferencesFlow.asLiveData()

    private val _authSignInURL = MutableLiveData<String>()
    val authSignInURL: LiveData<String> = _authSignInURL

    val loggedInAccounts = accountDao.getLoggedInAccounts()

    val accessToken = MutableLiveData<String>()
    val refreshToken = MutableLiveData<String>()
    private val username = MutableLiveData("")
    private val userData = MutableLiveData<UserInfo?>()
    private val isCompact = MutableLiveData<Boolean?>()

    val accountOverviewItems = Transformations.switchMap(MonitorPair(username,isCompact)) { pair ->
        redditApiRepository.getUserOverviewList(pair.first, userData.value, 0, pair.second).cachedIn(viewModelScope)

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
                val newLoggedInAccount = Account(
                    username = it,
                    refreshToken = refreshToken,
                    accessToken = accessToken
                )
                addAccountToApp(newLoggedInAccount)
                authDataStoreRepository.updateUsername(
                    it
                )
            }

        } else {
            Log.v(TAG, "Log in fail")
        }
    }



    // dialogs

    private fun addAccountToApp(account: Account) = viewModelScope.launch {
        accountDao.insert(account)
    }

    fun onAccountSwitch(account: Account) {
        println("HELLO ${account.username}")
    }

    fun onLogoutClicked() = viewModelScope.launch {
        Log.v(TAG, "HELLO Clearing user login data")

        try {
            //only need to revoke refresh token to also revoke all access tokens associated to it
            refreshToken.value?.let { authApiRepository.logoutUser(it, "refresh_token") }
        } catch (e: Exception) {

        }
        authDataStoreRepository.updateAccessToken("")
        authDataStoreRepository.updateRefreshToken("")
        authDataStoreRepository.updateUsername("")
        authDataStoreRepository.updateLoginState(false)

    }

    fun onAccountSwitch() = viewModelScope.launch {

    }



    fun onAccountsClicked() {
        _navigationActions.tryOffer(AccountOverviewNavigationAction.NavigateToAccountSelector)
    }




    init {
        _authSignInURL.value = String.format(
            AUTH_URL,
            BuildConfig.REDDIT_CLIENT_ID,
            BuildConfig.AUTH_STATE,
            BuildConfig.AUTH_REDIRECT_URI
        )

    }
    companion object {
        private const val AUTH_URL: String =
            "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" + "&response_type=code&state=%s&redirect_uri=%s&" + "duration=permanent&scope=identity edit flair history mysubreddits privatemessages read report save submit subscribe vote"

    }

}

sealed class AccountOverviewNavigationAction {
    object NavigateToAccountSelector : AccountOverviewNavigationAction()
}
