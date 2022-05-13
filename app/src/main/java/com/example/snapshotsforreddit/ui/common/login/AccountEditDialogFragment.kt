package com.example.snapshotsforreddit.ui.common.login

import android.R
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.snapshotsforreddit.ui.tabs.account.overview.AccountOverviewViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AccountEditDialogFragment: AppCompatDialogFragment() {
    private val viewModel: AccountOverviewViewModel by viewModels()

    private lateinit var listAdapter: ArrayAdapter<AccountLogin>


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //R.layout.simple_list_item_multiple_choice
        listAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_list_item_multiple_choice
        )

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Accounts")
            .setPositiveButton("Delete", null)
            .setNeutralButton("Cancel", null)
            .setSingleChoiceItems(listAdapter, 0) { dialog, position ->
                listAdapter.getItem(position)?.account?.let {
                    
                }

            }
            .create()

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


    companion object {
        fun newInstance() = AccountEditDialogFragment()
    }


}