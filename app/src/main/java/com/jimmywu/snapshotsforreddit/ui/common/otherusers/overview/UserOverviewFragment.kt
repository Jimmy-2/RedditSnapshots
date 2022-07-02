package com.jimmywu.snapshotsforreddit.ui.common.otherusers.overview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.jimmywu.snapshotsforreddit.R
import com.jimmywu.snapshotsforreddit.databinding.FragmentUserOverviewBinding
import com.jimmywu.snapshotsforreddit.network.responses.RedditChildrenData
import com.jimmywu.snapshotsforreddit.ui.common.loadstate.RedditLoadStateAdapter
import com.jimmywu.snapshotsforreddit.ui.tabs.account.overview.AccountOverviewAdapter
import com.jimmywu.snapshotsforreddit.ui.tabs.account.overview.UserInfoDialogFragment
import com.jimmywu.snapshotsforreddit.util.changeViewOnLoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserOverviewFragment : Fragment(R.layout.fragment_user_overview),
    AccountOverviewAdapter.OnItemClickListener, UserOverviewAdapter.OnItemClickListener {
    private val navigationArgs: UserOverviewFragmentArgs by navArgs()
    private val viewModel: UserOverviewViewModel by viewModels()

    private var _binding: FragmentUserOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUserOverviewBinding.bind(view)

        val userOverviewAdapter = UserOverviewAdapter(this)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigationActions.collect {
                if(it is UserOverviewNavigationAction.NavigateToUserInfo) {
                    if(viewModel.userInfo.value != null && viewModel.infoType.value != null) {
                        UserInfoDialogFragment.newInstance(viewModel.userInfo.value!!,
                            viewModel.infoType.value!!
                        ).show(parentFragmentManager, null)
                    }
                }
            }
        }

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

    override fun onInfoClick(infoItem: RedditChildrenData, type: Int) {
        viewModel.onInfoClicked(infoItem, type)

    }

    override fun onHistoryClick(historyType: String?, historyName: String?, userName: String?) {
        TODO("Not yet implemented")
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

    override fun onMoreClick(post: RedditChildrenData, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onSubredditClick(post: RedditChildrenData) {
        TODO("Not yet implemented")
    }


}