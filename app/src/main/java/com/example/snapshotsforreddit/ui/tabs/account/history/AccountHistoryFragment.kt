package com.example.snapshotsforreddit.ui.tabs.account.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentAccountHistoryBinding
import com.example.snapshotsforreddit.network.responses.Defaults
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.ui.common.loadstate.RedditLoadStateAdapter
import com.example.snapshotsforreddit.ui.tabs.account.overview.OverviewAdapter
import com.example.snapshotsforreddit.util.changeViewOnLoadState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountHistoryFragment : Fragment(R.layout.fragment_account_history), OverviewAdapter.OnItemClickListener {

    private val navigationArgs: AccountHistoryFragmentArgs by navArgs()

    val viewModel: AccountHistoryViewModel by viewModels()

    private var _binding: FragmentAccountHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentAccountHistoryBinding.bind(view)


        val accountHistoryAdapter = OverviewAdapter (this)

        binding.apply {
            recyclerviewAccountHistory.setHasFixedSize(true)
            recyclerviewAccountHistory.adapter = accountHistoryAdapter.withLoadStateHeaderAndFooter(
                header = RedditLoadStateAdapter {accountHistoryAdapter.retry()},
                footer = RedditLoadStateAdapter {accountHistoryAdapter.retry()}
            )
            refreshAccountHistory.setOnRefreshListener { accountHistoryAdapter.refresh()}
            buttonAccountHistoryRetry.setOnClickListener { accountHistoryAdapter.retry() }

        }

        val newUserHistory = UserHistory(navigationArgs.historyType, navigationArgs.username)
        viewModel.selectedUserHistory(newUserHistory)

        viewModel.accountHistoryItems.observe(viewLifecycleOwner) {
            accountHistoryAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }


        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
        accountHistoryAdapter.addLoadStateListener { loadState ->
            binding.apply {
                changeViewOnLoadState(
                    loadState,
                    accountHistoryAdapter.itemCount,
                    0,
                    progressbarAccountHistory,
                    recyclerviewAccountHistory,
                    buttonAccountHistoryRetry,
                    textviewAccountHistoryError,
                    textviewAccountHistoryEmpty,
                    refreshAccountHistory
                )
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onInfoClick(infoItem: RedditChildrenObject, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onHistoryClick(userDefaults: Defaults?) {
        TODO("Not yet implemented")
    }


    override fun onPostCommentClick(overviewItem: RedditChildrenObject, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemClick(post: RedditChildrenObject) {
        TODO("Not yet implemented")
    }

    override fun onVoteClick(post: RedditChildrenObject, type: Int) {
        TODO("Not yet implemented")
    }
}