package com.example.snapshotsforreddit.ui.redditpage

import android.util.Log
import androidx.lifecycle.*
import com.example.snapshotsforreddit.data.TokensDatastore
import com.example.snapshotsforreddit.network.responses.ChildrenData
import com.example.snapshotsforreddit.network.responses.ChildrenObject
import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.services.RedditApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class FrontPageViewModel(private val tokensDatastore: TokensDatastore): ViewModel() {

    var tokensDataStoreFlow = tokensDatastore.readTokensFromDataStore.asLiveData()

    private val _postItems = MutableLiveData<List<ChildrenData>>()
    val postItems: LiveData<List<ChildrenData>> = _postItems

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken



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

    fun setAccessToken(accessToken: String?) {
        _accessToken.value = accessToken!!
    }
}

class FrontPageViewModelFactory(private val tokensDatastore: TokensDatastore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FrontPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FrontPageViewModel(tokensDatastore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

