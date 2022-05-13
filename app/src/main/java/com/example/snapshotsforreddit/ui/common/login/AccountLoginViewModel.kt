package com.example.snapshotsforreddit.ui.common.login

import android.util.Log
import androidx.lifecycle.*
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.room.Account
import com.example.snapshotsforreddit.data.room.AccountDao
import com.example.snapshotsforreddit.network.AuthApiRepository
import com.example.snapshotsforreddit.util.tryOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountLoginViewModel @Inject constructor(
    private val accountDao: AccountDao,
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository
) : ViewModel() {

    private val _navigationActions = Channel<AccountLoginNavigationAction>(capacity = Channel.CONFLATED)


    val navigationActions = _navigationActions.receiveAsFlow()


    val loggedInAccounts = accountDao.getLoggedInAccounts()

    fun onAccountSwitch(account: Account) {
        println("HELLO ${account.username}")
    }


    private val TAG: String = "LoginViewModel"

    //observe login state
    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    private val _authSignInURL = MutableLiveData<String>()
    val authSignInURL: LiveData<String> = _authSignInURL


    val accessToken = MutableLiveData<String>()
    val refreshToken = MutableLiveData<String>()

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

    fun onEditButtonClicked() {
        println("HELLO DUDE")
        _navigationActions.tryOffer(AccountLoginNavigationAction.NavigateToEditSelector)
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
sealed class AccountLoginNavigationAction {
    object NavigateToEditSelector : AccountLoginNavigationAction()
}