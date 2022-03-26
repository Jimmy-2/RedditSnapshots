package com.example.snapshotsforreddit.model

import android.util.Base64
import android.util.Log
import androidx.lifecycle.*
import com.example.snapshotsforreddit.data.TokensDatastore
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.services.RedditApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response



class AuthViewModel(private val tokensDatastore: TokensDatastore): ViewModel() {

    fun saveCode(accessToken: String){
        viewModelScope.launch {
            tokensDatastore.saveAccessTokenToDataStore(accessToken)
        }
    }
    var tokensDatastoreFlow = tokensDatastore.readTokensFromDataStore.asLiveData()

    val state = STATE
    //testing
    private val _buttonText = MutableLiveData<String>()
    val buttonText: LiveData<String> = _buttonText


    private val _authSignInURL = MutableLiveData<String>()
    val authSignInURL: LiveData<String> = _authSignInURL

    init {
        _buttonText.value = "Access Token"
        getURIString()

    }
    private fun getURIString() {
        _authSignInURL.value = String.format(
            AUTH_URL, CLIENT_ID,
            STATE, REDIRECT_URI
        )
    }
    companion object {
        //make sure the redirect uri in your reddit apps is the same as this.
        //change the uri in the manifest too if you want to change this.
        private const val REDIRECT_URI = "http://www.snapshotstempaddress.com/random_redirect"

        //Enter your own client id here.
        private const val CLIENT_ID = "HIJeGKli5nnrL6xi7TPNhQ"
        private const val AUTH_URL: String = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
                "&response_type=code&state=%s&redirect_uri=%s&" +
                "duration=permanent&scope=identity account history read report save subscribe vote"
        private const val STATE = "random_string_to_check_if_correct_url"

    }
    //not sure if there is a need for async
    private fun getAccessToken(code: String) {
        Log.i("MainFragment", "code: $code")
        val authString = "${CLIENT_ID}:"
        val encodedAuthString: String = Base64.encodeToString(
            authString.toByteArray(),
            Base64.NO_WRAP
        )
        viewModelScope.launch {
            try {
                val request = RedditApi.retrofitServiceToken.getToken("Testing","Basic $encodedAuthString", "authorization_code", code,
                    REDIRECT_URI
                )
                request.enqueue(object : retrofit2.Callback<TokenResponse> {
                    override fun onResponse(
                        request: Call<TokenResponse>,
                        response: Response<TokenResponse>
                    ) {
                        val responseParse: TokenResponse? = response.body()
                        Log.i("MainFragment", "Authentication response: ${responseParse.toString()}")
                        val accessToken = responseParse?.access_token
                        val refreshToken = responseParse?.refresh_token
                        val token_type = responseParse?.token_type
                        if (accessToken != null) {
                            Log.i("MainFragment", "access token: $accessToken")
                            Log.i("MainFragment", "refresh token: $refreshToken")
                            _buttonText.value = accessToken!!
                            saveCode(accessToken)
                        } else {
                            println("Error: no access token")
                        }
                    }
                    override fun onFailure(request: Call<TokenResponse>, t: Throwable) {

                    }
                })
            }catch (e: Exception) {

            }

        }

    }
    private var code: String? = null
    fun setCode(code: String?) {
        this.code = code
        if (code != null) {
            getAccessToken(code)
        }
    }
}


class AuthViewModelFactory(private val tokensDatastore: TokensDatastore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(tokensDatastore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

