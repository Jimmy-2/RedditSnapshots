package com.example.snapshotsforreddit.ui.common.login

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AccountEditDialogFragment(private val accountsArray: Array<String>) : AppCompatDialogFragment() {
    private val viewModel: AccountDialogViewModel by viewModels()

    //TODO delete by id

    private fun deleteAccountsClickListener(accountsToRemove: ArrayList<String>) = { dialog: DialogInterface, position: Int ->
        for (accountUsername in accountsToRemove) {
            viewModel.onAccountsDelete(accountUsername)
        }
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val checkedAccounts = BooleanArray(accountsArray.size)
        for (item in checkedAccounts) {
            println("HELLO123 $item")
        }

        val selectedAccounts = ArrayList<String>()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Accounts")
            .setPositiveButton("Delete", deleteAccountsClickListener(selectedAccounts))
            .setNeutralButton("Cancel", null)
            // Specify the list array, the items to be selected by default (null for none),
            // and the listener through which to receive callbacks when items are selected
            .setMultiChoiceItems( accountsArray, null) { dialog, which, isChecked ->
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get list of logged in accounts and create a dialog list of them
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loggedInAccounts.collect() { accounts ->

//                    accountsList.clear()

//                    accountsList.addAll(
//                        accounts.map { account ->
//                            AccountLogin(account, account.username)
//                        }
//                    )
//                    test.clear()
//                    test.addAll(
//                        accounts.map { account ->
//                            account.username
//                        }
//                    )


                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }


    }


    companion object {
        fun newInstance(accountsArray: Array<String>) = AccountEditDialogFragment(accountsArray)
    }


}