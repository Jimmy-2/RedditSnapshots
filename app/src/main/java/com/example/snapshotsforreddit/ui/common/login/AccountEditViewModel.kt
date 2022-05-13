package com.example.snapshotsforreddit.ui.common.login

import androidx.lifecycle.ViewModel
import com.example.snapshotsforreddit.data.room.AccountDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountEditViewModel @Inject constructor(private val accountDao: AccountDao,): ViewModel() {
    val loggedInAccounts = accountDao.getLoggedInAccounts()
}