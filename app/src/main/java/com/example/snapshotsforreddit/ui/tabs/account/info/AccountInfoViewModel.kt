package com.example.snapshotsforreddit.ui.tabs.account.info

import androidx.lifecycle.ViewModel
import com.example.snapshotsforreddit.data.AuthDataStoreRepository
import com.example.snapshotsforreddit.network.AuthApiRepository
import com.example.snapshotsforreddit.network.RedditApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
    private val redditApiRepository: RedditApiRepository
) : ViewModel() {

}