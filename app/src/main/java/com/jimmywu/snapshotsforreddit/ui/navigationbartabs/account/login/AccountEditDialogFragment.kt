package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.account.login

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jimmywu.snapshotsforreddit.data.room.loggedinaccounts.Account
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountEditDialogFragment(private val accountsArray: Array<Account>, private val currentAccountUsername: String?) : AppCompatDialogFragment() {
    private val viewModel: AccountDialogViewModel by viewModels()

    private fun deleteAccountsClickListener(accountsToRemove: ArrayList<Account>) = { dialog: DialogInterface, position: Int ->
        //if no accounts are selected to be deleted, confirmation screen will not be displayed
        if(accountsToRemove.size > 0) {
            AccountConfirmationDialogFragment.newInstance(accountsToRemove, currentAccountUsername).show(parentFragmentManager,"")
        }

//        for (accountUsername in accountsToRemove) {
//            viewModel.onAccountsDelete(accountUsername)
//        }
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val usernameArray = accountsArray.map { Account -> Account.username }.toTypedArray()
        
        val selectedAccounts = ArrayList<Account>()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Accounts")
            .setPositiveButton("Delete", deleteAccountsClickListener(selectedAccounts))
            .setNeutralButton("Cancel", null)
            // Specify the list array, the items to be selected by default (null for none),
            // and the listener through which to receive callbacks when items are selected
            .setMultiChoiceItems(usernameArray, null) { dialog, which, isChecked ->
//                checkedAccounts[which] = isChecked
                if (isChecked) {
                    // If the user checked the item, add it to the selected items
                    selectedAccounts.add(accountsArray[which])
                } else if (selectedAccounts.contains(accountsArray[which])) {
                    // Else, if the item is already in the array, remove it
                    selectedAccounts.remove(accountsArray[which])
                }
            }.create()

    }

    companion object {
        fun newInstance(accountsArray: Array<Account>, currentAccountUsername: String?) = AccountEditDialogFragment(accountsArray, currentAccountUsername)
    }


}