package com.example.snapshotsforreddit.ui.tabs.account.overview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentAccountOverviewBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountOverviewFragment: Fragment(R.layout.fragment_account_overview), AccountAdapter.OnItemClickListener {
    private val viewModel: AccountOverviewViewModel by viewModels()

    private var _binding: FragmentAccountOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentAccountOverviewBinding.bind(view)

        val accountOverviewAdapter = AccountAdapter (this)

        binding.apply {
            recyclerviewAccount.setHasFixedSize(true)
            recyclerviewAccount.adapter = accountOverviewAdapter

            /*
            buttonDialog.setOnClickListener { loginView ->
                loginView.findNavController().navigate(
                    AccountOverviewFragmentDirections.actionAccountOverviewFragmentToLoginDialogFragment()
                )
            }

             */
            //swipeRefresh.setOnRefreshListener { accountOverviewAdapter.refresh() }
        }

        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->

            if(authFlowValues.username == "") {
                binding.recyclerviewAccount.visibility = View.GONE
            }else {
                binding.recyclerviewAccount.visibility = View.VISIBLE
            }
            viewModel.checkIfUsernameChanged(authFlowValues.username)
        }

        viewModel.accountOverviewItems.observe(viewLifecycleOwner) {
            //connect data to adapter
            accountOverviewAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        setHasOptionsMenu(true)
    }


    override fun onResume() {
        super.onResume()
        //on successful authentication
        if (requireActivity().intent != null && requireActivity().intent.action == Intent.ACTION_VIEW) {
            val uri: Uri? = requireActivity().intent.data
            viewModel.checkCode(uri)
        }
        //clear intent after successful retrieval of uri
        requireActivity().intent = null

    }

    //inflate/activate options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_account_overview, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //when statement for each of the menu items
        return when(item.itemId) {
            R.id.action_accounts_dialog -> {
                findNavController().navigate(
                    AccountOverviewFragmentDirections.actionAccountOverviewFragmentToLoginDialogFragment()
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onInfoClick(infoItem: RedditChildrenObject, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onHistoryClick(historyType: String?, username: String?) {
        if(historyType != null && username != null) {
            findNavController().navigate(AccountOverviewFragmentDirections.actionAccountOverviewFragmentToAccountHistoryFragment(historyType, username))
        }
    }

    override fun onPostCommentClick(overviewItem: RedditChildrenObject, type: Int) {
        TODO("Not yet implemented")
    }
}
