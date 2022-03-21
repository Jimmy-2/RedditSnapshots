package com.example.snapshotsforreddit.model

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapshotsforreddit.network.responses.ChildrenData
import com.example.snapshotsforreddit.network.responses.ChildrenObject
import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.services.RedditApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class RedditViewModel: ViewModel() {

    val state = STATE

    //testing
    private val _buttonText = MutableLiveData<String>()
    val buttonText: LiveData<String> = _buttonText


    private val _postItems = MutableLiveData<List<ChildrenData>>()
    val postItems: LiveData<List<ChildrenData>> = _postItems


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

    //put these into datastore and split the sharedviewmodel into separate viewmodels for different fragments
    companion object {
        //store access_token here
        //var access_token = ""
        //var refresh_token = ""

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
        val authString = "${Companion.CLIENT_ID}:"
        val encodedAuthString: String = Base64.encodeToString(
            authString.toByteArray(),
            Base64.NO_WRAP
        )
        val request = RedditApi.retrofitServiceToken.getToken("Testing","Basic $encodedAuthString", "authorization_code", code,
            Companion.REDIRECT_URI
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
                    //testing method to get list of posts
                    getPosts(accessToken, token_type)
                    //go to different fragment.

                } else {
                    println("Error: no access token")
                }
            }

            override fun onFailure(request: Call<TokenResponse>, t: Throwable) {

            }

        })


    }

    private fun saveTokens(accessToken: String?, refreshToken: String?) {

    }



    //if accesstoken doesn't work, refresh it with the refresh token or make user reauthenticate
    fun getPosts(accessToken: String?, token_type: String?) {
        viewModelScope.launch {
            try {
                val request = RedditApi.retrofitServiceOAuth.getListOfPosts(
                    "$token_type $accessToken",
                    "snapshots-for-reddit"
                )
                //val request = RedditApi.retrofitServiceOAuth.getListPost2()

                request.enqueue(object : retrofit2.Callback<RedditJsonResponse> {
                    override fun onResponse(
                        call: Call<RedditJsonResponse>,
                        response: Response<RedditJsonResponse>
                    ) {
                        val frontPageParse: RedditJsonResponse? = response.body()
                        val data = frontPageParse?.data
                        if (data != null) {
                            val children: List<ChildrenObject> = data.children
                            //listOfPosts is the jsonobject called data that can be found in children jsonarray
                            val listOfPosts: MutableList<ChildrenData> = mutableListOf()
                            //TODO("change to lambda after testing")
                            //Elvis operator
                            for (i in 0..children.size - 1) {
                                if (children[i].childrenData != null) {
                                    listOfPosts.add(children[i].childrenData!!)
                                }

                            }

                            _postItems.value = listOfPosts

                        } else {
                            Log.e("FrontPageFragment", "ERROR at getPosts")
                        }
                    }

                    override fun onFailure(call: Call<RedditJsonResponse>, t: Throwable) {
                    }
                })
            } catch (e: Exception) {
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