package com.example.snapshotsforreddit.ui.tabs.account.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentAccountHistoryBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.ui.RedditLoadStateAdapter
import com.example.snapshotsforreddit.ui.tabs.account.overview.AccountAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountHistoryFragment : Fragment(R.layout.fragment_account_history), AccountAdapter.OnItemClickListener {

    private val navigationArgs: AccountHistoryFragmentArgs by navArgs()

    val viewModel: AccountHistoryViewModel by viewModels()

    private var _binding: FragmentAccountHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentAccountHistoryBinding.bind(view)


        val accountHistoryAdapter = AccountAdapter (this)

        binding.apply {
            recyclerviewAccountHistory.setHasFixedSize(true)
            recyclerviewAccountHistory.adapter = accountHistoryAdapter.withLoadStateHeaderAndFooter(
                header = RedditLoadStateAdapter {accountHistoryAdapter.retry()},
                footer = RedditLoadStateAdapter {accountHistoryAdapter.retry()}
            )

        }

        val newUserHistory = UserHistory(navigationArgs.historyType, navigationArgs.username)
        viewModel.selectedUserHistory(newUserHistory)

        viewModel.accountHistoryItems.observe(viewLifecycleOwner) {
            accountHistoryAdapter.submitData(viewLifecycleOwner.lifecycle, it)
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
        TODO("Not yet implemented")
    }

    override fun onPostCommentClick(overviewItem: RedditChildrenObject, type: Int) {
        TODO("Not yet implemented")
    }
}