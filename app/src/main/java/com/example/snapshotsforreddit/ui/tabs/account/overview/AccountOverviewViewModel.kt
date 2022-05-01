package com.example.snapshotsforreddit.ui.tabs.account.overview

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.AuthDataStoreRepository
import com.example.snapshotsforreddit.network.AuthApiRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.responses.account.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AccountOverviewViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {
    private val TAG: String = "AccountOverviewViewModel"

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    private val _username = MutableLiveData("")
    private val _userData = MutableLiveData<UserInfo?>()

    val accountOverviewItems = _username.switchMap { username ->
        redditApiRepository.getUserOverviewList(username, _userData.value, 0).cachedIn(viewModelScope)

    }

    fun checkIfUsernameChanged(username: String) {
        //only if accessToken changes do we update subreddits
        if (_username.value != username) {
            //_username.value = username
            getLoggedInUserData(username)
        }
    }

    private fun getLoggedInUserData(username: String) = viewModelScope.launch {
        try {
            _userData.value = redditApiRepository.getUserInfoData(username).data
            _username.value = _userData.value?.name
        } catch (e: Exception) {

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
                authDataStoreRepository.updateUsername(
                    it
                )
            }
            Log.v(TAG, "HELLO Access Token: $accessToken")
            Log.v(TAG, "HELLO Refresh Token: $refreshToken")


        } else {
            Log.v(TAG, "Log in fail")
        }
    }


}

