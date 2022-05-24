package com.example.snapshotsforreddit.ui.tabs.account.overview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentAccountOverviewBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.ui.common.loadstate.RedditLoadStateAdapter
import com.example.snapshotsforreddit.ui.common.login.AccountLoginDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AccountOverviewFragment: Fragment(R.layout.fragment_account_overview), OverviewAdapter.OnItemClickListener {
    private val viewModel: AccountOverviewViewModel by viewModels()

    private var _binding: FragmentAccountOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentAccountOverviewBinding.bind(view)

        val accountOverviewAdapter = OverviewAdapter (this)

        //(requireActivity() as AppCompatActivity).supportActionBar?.title = TODO: username

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigationActions.collect {
                if (it is AccountOverviewNavigationAction.NavigateToAccountSelector) {
                    AccountLoginDialogFragment.newInstance()
                        .show(parentFragmentManager, null)
                }
            }
        }


        binding.apply {
            recyclerviewAccountOverview.setHasFixedSize(true)
            recyclerviewAccountOverview.adapter = accountOverviewAdapter.withLoadStateHeaderAndFooter(
                header = RedditLoadStateAdapter {accountOverviewAdapter.retry()},
                footer = RedditLoadStateAdapter {accountOverviewAdapter.retry()}
            )
            refreshAccountOverview.setOnRefreshListener { accountOverviewAdapter.refresh()}
            buttonAccountOverviewRetry.setOnClickListener { accountOverviewAdapter.retry() }
        }

        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            if(authFlowValues.username == "") {
                binding.recyclerviewAccountOverview.visibility = View.GONE
            }else {
                //TODO display a log in to check account info dialog
                binding.recyclerviewAccountOverview.visibility = View.VISIBLE
            }
            viewModel.checkIfUsernameChanged(authFlowValues.username)
        }

        viewModel.preferencesFlow.observe(viewLifecycleOwner) { preferencesFlowValues ->
            viewModel.checkIsCompact(preferencesFlowValues.isCompactView)
        }


        viewModel.accountOverviewItems.observe(viewLifecycleOwner) {
            //connect data to adapter
            (activity as AppCompatActivity).supportActionBar?.title = viewModel.username.value
            accountOverviewAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }



        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
        accountOverviewAdapter.addLoadStateListener { loadState ->
            binding.apply {
//                changeViewOnLoadState(
//                    loadState,
//                    accountOverviewAdapter.itemCount,
//                    0,
//                    progressbarAccountOverview,
//                    recyclerviewAccountOverview,
//                    buttonAccountOverviewRetry,
//                    textviewAccountOverviewError,
//                    textviewAccountOverviewEmpty,
//                    refreshAccountOverview
//                )

                progressbarAccountOverview.isVisible = loadState.source.refresh is LoadState.Loading
                refreshAccountOverview.isRefreshing = loadState.mediator?.refresh is LoadState.Loading
                //add a login button

            }
        }


        setHasOptionsMenu(true)
    }


    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = viewModel.username.value

        //on successful authentication
        if (requireActivity().intent != null && requireActivity().intent.action == Intent.ACTION_VIEW) {
            val uri: Uri? = requireActivity().intent.data
            viewModel.checkCode(uri)
        }
        //clear intent after successful retrieval of uri
        requireActivity().intent = null

        //TODO REFRESH ON RESUME


    }

    //inflate/activate options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_account_overview, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //when statement for each of the menu items
        return when(item.itemId) {
            R.id.action_accounts_dialog -> {
                viewModel.onAccountsClicked()
//                findNavController().navigate(
//                    AccountOverviewFragmentDirections.actionAccountOverviewFragmentToLoginDialogFragment()
//                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onInfoClick(infoItem: RedditChildrenData, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onHistoryClick(historyType: String?, historyName: String?, userName: String?) {

        if(historyType != null && historyName != null && userName != null) {
            findNavController().navigate(AccountOverviewFragmentDirections.actionAccountOverviewFragmentToAccountHistoryFragment(historyType, historyName, userName))
        }
    }

    override fun onPostCommentClick(overviewItem: RedditChildrenData, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(post: RedditChildrenData) {
        TODO("Not yet implemented")
    }


    override fun onVoteClick(post: RedditChildrenData, type: Int) {
        TODO("Not yet implemented")
    }
}

