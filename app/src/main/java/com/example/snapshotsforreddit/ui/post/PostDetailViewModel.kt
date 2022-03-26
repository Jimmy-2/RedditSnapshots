package com.example.snapshotsforreddit.ui.post

import android.util.Log
import androidx.lifecycle.*
import com.example.snapshotsforreddit.data.TokensDatastore
import com.example.snapshotsforreddit.database.Post
import com.example.snapshotsforreddit.database.PostDao
import com.example.snapshotsforreddit.network.responses.ChildrenData
import com.example.snapshotsforreddit.network.responses.RedditJsonResponse
import com.example.snapshotsforreddit.network.services.RedditApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

//allow user to save a post to database
//dao
class PostDetailViewModel(private val postDao: PostDao, private val tokensDatastore: TokensDatastore) : ViewModel() {

    //when a user saves or upvotes a post, post to the api. and then get the result back from json.
    //do not refresh entire post

    var tokensDatastoreFlow = tokensDatastore.readTokensFromDataStore.asLiveData()

    private val _postSubreddit = MutableLiveData<String>()
    val postSubreddit : LiveData<String> = _postSubreddit
    private val _postId = MutableLiveData<String>()
    val postId : LiveData<String> = _postId

    //may be better to pass the information from the front page and then just update it if necessary

    private val _postDetails = MutableLiveData<List<RedditJsonResponse>>()
    val postDetails: LiveData<List<RedditJsonResponse>> = _postDetails



    private val _postInformation = MutableLiveData<ChildrenData?>()
    val postInformation: LiveData<ChildrenData?> = _postInformation


    override fun onCleared() {
        super.onCleared()
        println("HELLO VM CLEARED")
    }


    private val _postComments = MutableLiveData<ChildrenData>()
    val postComments : LiveData<ChildrenData> = _postComments

    private val _postTitle= MutableLiveData<String>()
    val postTitle: LiveData<String> = _postTitle
    private val _postImageUrl= MutableLiveData<String>()
    val postImageUrl : LiveData<String> = _postImageUrl

    fun retrievePostData(postData: ChildrenData?) {
        _postInformation.value = postData!!
    }


    init {

    }


    //add new post to database
    fun addNewPost() {
        println("ADDED")
        val newPost = getNewPostEntry("test title", "DSSSSSSSS")
        insertPost(newPost)

    }
    //Launching a new coroutine to add post to database in an asynchronous way
    private fun insertPost(post: Post) {
        viewModelScope.launch {
            postDao.insert(post)
        }
    }

    private fun getNewPostEntry(title: String, link: String): Post {
        return Post(
            title = title,
            permalink = link
        )
    }

    fun getPostDetail(accessToken: String?, token_type: String?) {

        viewModelScope.launch {
            try {
                //unfortunately we cannot just pass in a permalink as the "/" in the link gets formatted to become "%2F"
                //val requestTest = RedditApi.retrofitServiceTest.getPostDetailsTest(_postLink.value.toString())

                val request = RedditApi.retrofitServiceOAuth.getPostDetails(
                    "$token_type $accessToken",
                    "snapshots-for-reddit",
                    _postSubreddit.value, _postId.value
                )
                val requestTest = RedditApi.retrofitServiceTest.getPostDetailsTest(_postSubreddit.value, _postId.value)


                requestTest.enqueue(object : retrofit2.Callback<List<RedditJsonResponse>> {
                    override fun onResponse(
                        call: Call<List<RedditJsonResponse>>,
                        response: Response<List<RedditJsonResponse>>
                    ) {
                        Log.i("FrontPageFragment", response.raw().toString())
                        //A list of redditjsonresponses of size 2
                        //the 2nd redditjsonresponse of the list will contain all the comments of the post
                        val postDetailsParse: List<RedditJsonResponse>? = response.body()
                        val postDetailItem1: RedditJsonResponse = postDetailsParse!![0]

                        val data = postDetailItem1!!.data

                        if (data != null && data.dist == 1) {
                            //size will be 1 at index 0
                            _postInformation.value = data.children[0].childrenData!!
                        } else {
                            Log.e("FrontPageFragment", "ERROR at getPosts")
                        }
                    }

                    override fun onFailure(call: Call<List<RedditJsonResponse>>, t: Throwable) {
                    }
                })
            } catch (e: Exception) {
            }
        }

    }

    fun resetView() {
        _postInformation.value = null
    }













}
class PostDetailViewModelFactory(private val postDao: PostDao, private val tokensDatastore: TokensDatastore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostDetailViewModel(postDao, tokensDatastore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}