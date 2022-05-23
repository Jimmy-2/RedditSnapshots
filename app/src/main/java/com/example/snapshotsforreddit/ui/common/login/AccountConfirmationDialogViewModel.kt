package com.example.snapshotsforreddit.ui.common.login

import androidx.lifecycle.ViewModel
import com.example.snapshotsforreddit.data.datastore.AuthDataStoreRepository
import com.example.snapshotsforreddit.data.room.loggedinaccounts.Account
import com.example.snapshotsforreddit.data.room.loggedinaccounts.AccountDao
import com.example.snapshotsforreddit.di.ApplicationScope
import com.example.snapshotsforreddit.network.AuthApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountConfirmationDialogViewModel @Inject constructor(
    private val authDataStoreRepository: AuthDataStoreRepository,
    private val authApiRepository: AuthApiRepository,
    private val accountDao: AccountDao,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    fun onConfirmDelete(accountToDelete: Account, currentAccountUsername: String?) = applicationScope.launch {
        try {
            authApiRepository.logoutUser(accountToDelete.refreshToken,"refresh_token")
        }catch (e: Exception) {

        }
        accountDao.delete(accountToDelete)
        if(currentAccountUsername == accountToDelete.username) {
            authDataStoreRepository.updateCurrentAccount("","","",false)
        }
    }

}