package com.example.snapshotsforreddit.ui.tabs.account.login

import android.R
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.snapshotsforreddit.data.room.loggedinaccounts.Account
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

data class AccountUsername(val account: Account, val username: String) {
    override fun toString(): String = username
}
@AndroidEntryPoint
class AccountLoginDialogFragment: AppCompatDialogFragment()  {
    private val viewModel: AccountDialogViewModel by viewModels()

    private lateinit var listAdapter: ArrayAdapter<AccountUsername>

    private lateinit var accountsList: ArrayList<Account>

    private var currentAccountUsername: String? = null

    private val addAccountClickListener = { dialog: DialogInterface, position: Int ->
        val intent = Uri.parse(viewModel.authSignInURL.value)
        val actionView = Intent(Intent.ACTION_VIEW, intent)
        startActivity(actionView)
    }

    private val editAccountClickListener = { dialog: DialogInterface, position: Int ->
        val accountsArray = accountsList.toTypedArray()
        AccountEditDialogFragment.newInstance(accountsArray, currentAccountUsername).show(parentFragmentManager,"")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //R.layout.simple_list_item_multiple_choice
        listAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_list_item_single_choice
        )

        accountsList = ArrayList()

        val dialog =  MaterialAlertDialogBuilder(requireContext())
            .setTitle("Accounts")
            .setPositiveButton("Add Account",   addAccountClickListener)
            .setNeutralButton("Edit", editAccountClickListener)
            .setSingleChoiceItems(listAdapter, 0) { dialog, which ->
                listAdapter.getItem(which)?.account?.let {
                    //viewModel.onAccountSwitch(it.username,it.refreshToken,it.accessToken)
                    viewModel.onAccountSwitch(it.username,it.refreshToken,it.accessToken)
                }
                dialog.dismiss()
            }
            .create()
//        dialog.setOnShowListener {
//            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
//                AccountEditDialogFragment.newInstance().show(parentFragmentManager,"");
//                dialog.dismiss()
//            }
//
//        }

        return dialog
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get list of logged in accounts and create a dialog list of them
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loggedInAccounts.collect() { accounts ->
                    listAdapter.clear()
                    listAdapter.addAll(
                        accounts.map { account ->
                            AccountUsername(account, getAccountUsername(account))
                        }
                    )
                    accountsList.clear()
                    accountsList.addAll(
                        accounts
                    )
                    updateCurrentAccount(viewModel.currentUsername.value)
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUsername.collect {
                    updateCurrentAccount(it)
                }
            }
        }
    }

    //sets the current logged in account's ischeck to true
    private fun updateCurrentAccount(selectedAccount: String?) {
        currentAccountUsername = selectedAccount
        val selectedPosition = (0 until listAdapter.count).indexOfFirst { index ->
            listAdapter.getItem(index)?.account?.username == selectedAccount
        }
        (dialog as AlertDialog).listView.setItemChecked(selectedPosition, true)
    }

    private fun getAccountUsername(account: Account): String {
       return account.username
    }


    companion object {
        fun newInstance() = AccountLoginDialogFragment()
    }
}