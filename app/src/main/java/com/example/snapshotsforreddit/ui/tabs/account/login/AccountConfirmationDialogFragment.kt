package com.example.snapshotsforreddit.ui.tabs.account.login

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.example.snapshotsforreddit.data.room.loggedinaccounts.Account
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountConfirmationDialogFragment(private val accountsToRemove: ArrayList<Account>, private val currentAccountUsername: String?) :
    AppCompatDialogFragment() {

    private val viewModel: AccountConfirmationDialogViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm deletion?")
//            .setMessage("Do you really want to remove selected accounts?")
            .setNeutralButton("Cancel", null)
            .setPositiveButton("Confirm") { _, _ ->
                for (account in accountsToRemove) {
                    viewModel.onConfirmDelete(account, currentAccountUsername)
                }

            }
            .create()



    companion object {
        fun newInstance(accountsToRemove: ArrayList<Account>, currentAccountUsername: String?) =
            AccountConfirmationDialogFragment(accountsToRemove, currentAccountUsername)
    }
}