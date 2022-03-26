package com.example.snapshotsforreddit.ui.downloadedposts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.snapshotsforreddit.database.Post
import com.example.snapshotsforreddit.database.PostDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadedPostsTestViewModel @Inject constructor(
    private val postDao: PostDao
): ViewModel() {

    //automatically get new data from this flow if any changes occur to database
    val downloadedPosts: LiveData<List<Post>> = postDao.getDownloadedPosts().asLiveData()
}