package com.jimmywu.snapshotsforreddit.ui.tabs.account.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimmywu.snapshotsforreddit.BuildConfig
import com.jimmywu.snapshotsforreddit.data.datastore.AuthDataStoreRepository
import com.jimmywu.snapshotsforreddit.data.room.loggedinaccounts.AccountDao
import com.jimmywu.snapshotsforreddit.ui.tabs.settings.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountDialogViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val accountDao: AccountDao,
) : ViewModel() {
    private val refreshSignal = MutableSharedFlow<Unit>()

    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    val loggedInAccounts = accountDao.getLoggedInAccounts()

    private val _authSignInURL = MutableLiveData<String>()
    val authSignInURL: LiveData<String> = _authSignInURL


    val currentUsername: StateFlow<String> = loadDataSignal.mapLatest {
        getAccount()
    }.stateIn(viewModelScope, WhileViewSubscribed,"Anonymous")


    private suspend fun getAccount(): String {
        return authDataStoreRepository.currentUsername.first()
    }


    fun onAccountSwitch(username: String, refreshToken: String, accessToken: String) = viewModelScope.launch{
        authDataStoreRepository.updateCurrentAccount(username, refreshToken, accessToken,true)
    }


//    fun onAccountsDelete(username: String) = viewModelScope.launch {
//        accountDao.deleteAccount(username)
//    }

    fun onLogoutClicked() = viewModelScope.launch {


        try {
            //only need to revoke refresh token to also revoke all access tokens associated to it
//            refreshToken.value?.let { authApiRepository.logoutUser(it, "refresh_token") }
        } catch (e: Exception) {

        }
        authDataStoreRepository.updateCurrentAccount("","","",false)

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
