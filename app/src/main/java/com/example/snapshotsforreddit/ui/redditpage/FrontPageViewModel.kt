package com.example.snapshotsforreddit.ui.redditpage

import android.util.Log
import androidx.lifecycle.*
import com.example.snapshotsforreddit.data.Repository.AuthDataStoreRepository
import com.example.snapshotsforreddit.network.responses.ChildrenData
import com.example.snapshotsforreddit.network.responses.ChildrenObject
import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.services.RedditApiTest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FrontPageViewModel@Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository
) : ViewModel() {

    val authFlow = authDataStoreRepository.authFlow.asLiveData()

    private val _postItems = MutableLiveData<List<ChildrenData>>()
    val postItems: LiveData<List<ChildrenData>> = _postItems


    //if accesstoken doesn't work, refresh it with the refresh token or make user reauthenticate
    fun getPosts(accessToken: String?, token_type: String?) {
        viewModelScope.launch {
            try {
                val request = RedditApiTest.RETROFIT_SERVICE_TEST_O_AUTH.getListOfPosts(
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
                                if (children[i].data != null) {
                                    listOfPosts.add(children[i].data!!)
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

}

