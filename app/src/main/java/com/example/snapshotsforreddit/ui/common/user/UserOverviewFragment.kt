package com.example.snapshotsforreddit.ui.common.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentUserOverviewBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.ui.common.RedditLoadStateAdapter
import com.example.snapshotsforreddit.ui.tabs.account.overview.AccountOverviewAdapter
import com.example.snapshotsforreddit.util.changeViewOnLoadState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserOverviewFragment : Fragment(R.layout.fragment_user_overview), AccountOverviewAdapter.OnItemClickListener {
    private val navigationArgs: UserOverviewFragmentArgs by navArgs()
    private val viewModel: UserOverviewViewModel by viewModels()

    private var _binding: FragmentUserOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUserOverviewBinding.bind(view)

        val userOverviewAdapter = AccountOverviewAdapter(this)

        binding.apply {
            recyclerviewUserOverview.setHasFixedSize(true)
            recyclerviewUserOverview.adapter = userOverviewAdapter.withLoadStateHeaderAndFooter(
                header = RedditLoadStateAdapter { userOverviewAdapter.retry() },
                footer = RedditLoadStateAdapter { userOverviewAdapter.retry() }
            )
            refreshUserOverview.setOnRefreshListener { userOverviewAdapter.refresh() }
            buttonUserOverviewRetry.setOnClickListener { userOverviewAdapter.retry() }
        }

        val currentUserQuery = navigationArgs.username
        viewModel.changeUserQuery(currentUserQuery)



        viewModel.userOverviewItems.observe(viewLifecycleOwner) {
            //connect data to adapter
            userOverviewAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
        userOverviewAdapter.addLoadStateListener { loadState ->
            binding.apply {
                changeViewOnLoadState(
                    loadState,
                    userOverviewAdapter.itemCount,
                    0,
                    progressbarUserOverview,
                    recyclerviewUserOverview,
                    buttonUserOverviewRetry,
                    textviewUserOverviewError,
                    textviewUserOverviewEmpty,
                    refreshUserOverview
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

    override fun onHistoryClick(historyType: String?, username: String?) {
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