package com.example.snapshotsforreddit.ui.common.login

import android.R
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.snapshotsforreddit.data.room.Account
import com.example.snapshotsforreddit.ui.tabs.account.overview.AccountOverviewViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

data class AccountLogin(val account: Account, val username: String) {
    override fun toString(): String = username
}
@AndroidEntryPoint
class AccountLoginDialogFragment: AppCompatDialogFragment()  {
    private val viewModel: AccountOverviewViewModel by viewModels()

    private lateinit var listAdapter: ArrayAdapter<AccountLogin>

    private val addAccountClickListener = { dialog: DialogInterface, position: Int ->
        val intent = Uri.parse(viewModel.authSignInURL.value)
        val actionView = Intent(Intent.ACTION_VIEW, intent)
        startActivity(actionView)
    }

    private val editAccountClickListener = { dialog: DialogInterface, position: Int ->
        println("HELLO WORLD")
        AccountEditDialogFragment.newInstance().show(parentFragmentManager,"");

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //R.layout.simple_list_item_multiple_choice
        listAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_list_item_single_choice
        )

        val dialog =  MaterialAlertDialogBuilder(requireContext())
            .setTitle("Accounts")
            .setPositiveButton("Add Account",   addAccountClickListener)
            .setNeutralButton("Edit", editAccountClickListener)
            .setSingleChoiceItems(listAdapter, 0) { dialog, position ->
                listAdapter.getItem(position)?.account?.let {
                    viewModel.onAccountSwitch(it)
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
                            AccountLogin(account, account.username)
                        }
                    )

                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }


    companion object {
        fun newInstance() = AccountLoginDialogFragment()
    }
}