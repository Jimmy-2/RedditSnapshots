package com.example.snapshotsforreddit.ui.general.login

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.repository.AuthApiRepository
import com.example.snapshotsforreddit.data.repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.network.responses.TokenResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
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

    private fun getAccessToken(code: String) = viewModelScope.launch {
        try {
            onAccessTokensUpdated(authApiRepository.getTokenValues(code))
        } catch (e: Exception) {

        }


    }
    fun resetAccessTokenTest() = viewModelScope.launch {
        val thing = authDataStoreRepository.getValsFromPreferencesStore().first().refreshToken
        println("HELLO $thing")
        try {
            println("HELLO ${authApiRepository.getNewAccessToken(thing)}")
        } catch (e: Exception) {
            println("HELLO FAIL!")
        }


    }



    private fun onAccessTokensUpdated(tokenResponse: TokenResponse) = viewModelScope.launch {
        //update the sort order value in datastore on sort button clicked
        val accessToken = tokenResponse.access_token
        val refreshToken = tokenResponse.refresh_token
        if (accessToken != null && refreshToken != null) {
            authDataStoreRepository.updateAccessToken(accessToken)
            authDataStoreRepository.updateRefreshToken(refreshToken)
            authDataStoreRepository.updateLoginState(true)
            println("HELLO NEW REFRESHTOKEN $accessToken")
            println("HELLO NEW REFRESHTOKEN $refreshToken")
        } else {
            authDataStoreRepository.updateLoginState(false)
        }


    }

    private fun onAccessTokensUpdatedTest(tokenResponse: TokenResponse) = viewModelScope.launch {
        //update the sort order value in datastore on sort button clicked
        val accessToken = tokenResponse.access_token
        val refreshToken = tokenResponse.refresh_token
        if (accessToken != null && refreshToken != null) {
            authDataStoreRepository.updateAccessToken(accessToken)
            authDataStoreRepository.updateRefreshToken(refreshToken)
            authDataStoreRepository.updateLoginState(true)
            println("HELLO NEW REFRESHTOKEN $accessToken")
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

            } else {
                //TODO: Display error to user
            }
        }

    }

}