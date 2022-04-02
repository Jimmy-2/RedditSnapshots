package com.example.snapshotsforreddit.ui.home

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.Repository.AuthApiRepository
import com.example.snapshotsforreddit.data.Repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.Repository.RedditApiRepository
import com.example.snapshotsforreddit.network.responses.TokenResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribedSubredditsViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {


    private val _authSignInURL = MutableLiveData<String>()
    val authSignInURL: LiveData<String> = _authSignInURL

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
            "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" + "&response_type=code&state=%s&redirect_uri=%s&" + "duration=permanent&scope=identity account history read report save subscribe mysubreddits vote"

    }

    //TODO: create a login fragment and viewmodel that will cover a small part of the screen that will allow disconnected users to log in from any screen. Each screen will have a login button that will go to the login screen
    private fun getAccessToken(code: String) = viewModelScope.launch {
        println("HELLO CODE: $code")
        try {
            onAccessTokensUpdated(authApiRepository.getTokenValues(code))
            println("HELLO AUTHENTICATING")
        } catch (e: Exception) {
            println("HELLO NO WORK")
        }

    }

    fun onAccessTokensUpdated(tokenResponse: TokenResponse) = viewModelScope.launch {
        //update the sort order value in datastore on sort button clicked
        val accessToken = tokenResponse.access_token
        val refreshToken = tokenResponse.access_token
        if (accessToken != null && refreshToken != null) {
            authDataStoreRepository.updateAccessToken(accessToken)
            authDataStoreRepository.updateRefreshToken(refreshToken)
            authDataStoreRepository.updateLoginState(true)
        } else {
            authDataStoreRepository.updateLoginState(false)
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
                getAccessToken(currCode)
                println("HELLO CODE: $currCode")

            } else {
                //TODO: Display error to user
            }
        }

    }








    //////////////////////////////move everything on top to login viewmodel


    val authFlow = authDataStoreRepository.authFlow.asLiveData()
    private val _accessToken = MutableLiveData<String>()
    val subreddits = _accessToken.switchMap { accessT ->
        redditApiRepository.getSubscribedSubredditsResults(accessT).cachedIn(viewModelScope)

    }
    fun checkIfAccessTokenChanged(accessToken: String) = viewModelScope.launch {
        //only if accessToken changes do we update subreddits
        if (_accessToken.value != accessToken) {
            _accessToken.value = accessToken

            //save the username in datastore. //if unable to get username, the user must relog because refresh token expired or an error with login
            getLoggedInUsername(accessToken)
        }
    }

    private fun getLoggedInUsername(accessToken: String) = viewModelScope.launch {
        try {
            val request = redditApiRepository.getUsername(accessToken).name
            println(request)
        } catch (e: Exception) {

        }
    }








}