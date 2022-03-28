package com.example.snapshotsforreddit.ui.home

import android.content.ContentValues
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.snapshotsforreddit.BuildConfig
import com.example.snapshotsforreddit.data.Repository.AuthApiRepository
import com.example.snapshotsforreddit.data.Repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.Repository.RedditRepository
import com.example.snapshotsforreddit.network.responses.TokenResponse
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedJsonResponse
import com.example.snapshotsforreddit.network.services.RedditApiTest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SubscribedSubredditsViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
    private val redditRepository: RedditRepository
) : ViewModel() {

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    private val _authSignInURL = MutableLiveData<String>()
    val authSignInURL: LiveData<String> = _authSignInURL

    private val _subItemsTest = MutableLiveData<List<SubscribedChildrenObject>>()
    val subItemsTest: LiveData<List<SubscribedChildrenObject>> = _subItemsTest

    init {
        _authSignInURL.value = String.format(
            AUTH_URL, BuildConfig.REDDIT_CLIENT_ID,
            BuildConfig.AUTH_STATE, BuildConfig.AUTH_REDIRECT_URI
        )
    }

    companion object {
        //make sure the redirect uri in your reddit apps is the same as this.
        //change the uri in the manifest too if you want to change this.
        private const val REDIRECT_URI = "http://www.snapshotstempaddress.com/random_redirect"

        //Enter your own client id here.
        private const val CLIENT_ID = "HIJeGKli5nnrL6xi7TPNhQ"
        private const val AUTH_URL: String =
            "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" + "&response_type=code&state=%s&redirect_uri=%s&" + "duration=permanent&scope=identity account history read report save subscribe mysubreddits vote"
        private const val STATE = "random_string_to_check_if_correct_url"

    }

    //make this function a global usable function
    private fun getAccessToken(code: String) =
        viewModelScope.launch {
            Log.i("MainFragment", "code: $code")
            val authString = "${CLIENT_ID}:"
            val encodedAuthString: String = Base64.encodeToString(
                authString.toByteArray(),
                Base64.NO_WRAP
            )
            try {
                val request = RedditApiTest.retrofitServiceToken.getToken(
                    "Testing", "Basic $encodedAuthString", "authorization_code", code,
                    REDIRECT_URI
                )
                request.enqueue(object : retrofit2.Callback<TokenResponse> {
                    override fun onResponse(
                        request: Call<TokenResponse>,
                        response: Response<TokenResponse>
                    ) {
                        val responseParse: TokenResponse? = response.body()
                        println(response.raw().toString())
                        Log.i(
                            "MainFragment",
                            "Authentication response: ${responseParse.toString()}"
                        )
                        val accessToken = responseParse?.access_token
                        val refreshToken = responseParse?.refresh_token
                        val token_type = responseParse?.token_type
                        if (accessToken != null && refreshToken != null) {
                            Log.i("MainFragment", "access token: $accessToken")
                            Log.i("MainFragment", "refresh token: $refreshToken")
                            onAccessTokensUpdated(accessToken, refreshToken, true)


                        } else {
                            println("Error: no access token")
                        }
                    }

                    override fun onFailure(request: Call<TokenResponse>, t: Throwable) {

                    }
                })
            } catch (e: Exception) {
            }
        }



    private val at = MutableLiveData<String>()


    val subreddits = at.switchMap{ accessT ->
        redditRepository.getSubscribedSubredditsResults(accessT).cachedIn(viewModelScope)

    }


    fun changeAT(accessToken: String) = viewModelScope.launch {
        //only if accessToken changes do we update subreddits
        if(at.value != accessToken) {
            at.value = accessToken
        }

    }


    fun getSubscribedSubreddits(accessToken: String) = viewModelScope.launch {
        try {
            val request = RedditApiTest.retrofitServiceOAuth.getSubscribedList(
                "bearer $accessToken",
                "snapshots-for-reddit"
            )
            //val request = RedditApi.retrofitServiceOAuth.getListPost2()

            request.enqueue(object : retrofit2.Callback<SubscribedJsonResponse> {

                override fun onResponse(
                    call: Call<SubscribedJsonResponse>,
                    response: Response<SubscribedJsonResponse>
                ) {
                    println(response.raw().toString())
                    val frontPageParse: SubscribedJsonResponse? = response.body()
                    val data = frontPageParse?.data
                    if (data != null) {
                        val children: List<SubscribedChildrenObject> =
                            data.children.sortedBy { it.data!!.display_name_prefixed!!.substring(2) }
                        //listOfPosts is the jsonobject called data that can be found in children jsonarray

                        _subItemsTest.value = children


                    } else {
                        Log.e("FrontPageFragment", "ERROR at getPosts")
                    }
                }

                override fun onFailure(call: Call<SubscribedJsonResponse>, t: Throwable) {

                }
            })
        } catch (e: Exception) {

        }
    }

    fun onAccessTokensUpdated(accessToken: String, refreshToken: String, loginState: Boolean) =
        viewModelScope.launch {
            //update the sort order value in datastore on sort button clicked
            authDataStoreRepository.updateAccessToken(accessToken)
            authDataStoreRepository.updateRefreshToken(refreshToken)
            authDataStoreRepository.updateLoginState(loginState)

        }


    fun checkCode(uri: Uri?) {
        if (uri!!.getQueryParameter("error") != null) {
            val error = uri.getQueryParameter("error")
            Log.e(ContentValues.TAG, "An error has occurred : $error")
        } else {
            val currState = uri.getQueryParameter("state")
            val currCode = uri.getQueryParameter("code")
            if (currState == STATE && currCode != null) {

                getAccessToken(currCode)

            } else {

            }
        }

    }


}