package com.example.snapshotsforreddit.ui.general.redditpage

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentRedditPageBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.ui.general.RedditLoadStateAdapter
import com.example.snapshotsforreddit.util.changeViewOnLoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RedditPageFragment : Fragment(R.layout.fragment_reddit_page),
    RedditPageAdapter.OnItemClickListener {
    private val navigationArgs: RedditPageFragmentArgs by navArgs()
    private val viewModel: RedditPageViewModel by viewModels()

    private var _binding: FragmentRedditPageBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRedditPageBinding.bind(view)

        val redditPageAdapter = RedditPageAdapter(this)

        binding.apply {
            recyclerviewPosts.setHasFixedSize(true)
            recyclerviewPosts.adapter = redditPageAdapter.withLoadStateHeaderAndFooter(
                header = RedditLoadStateAdapter { redditPageAdapter.retry() },
                footer = RedditLoadStateAdapter { redditPageAdapter.retry() }
            )
            refreshRedditPage.setOnRefreshListener { redditPageAdapter.refresh() }
            buttonRedditPageRetry.setOnClickListener { redditPageAdapter.retry() }
        }

        val redditPageName = navigationArgs.redditPageName
        val redditPageType = navigationArgs.redditPageType
        viewModel.changeRedditPage(redditPageName, redditPageType)

        viewModel.redditPagePosts.observe(viewLifecycleOwner) {
            //connect data to adapter
            redditPageAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
        redditPageAdapter.addLoadStateListener { loadState: CombinedLoadStates ->
            binding.apply {
                changeViewOnLoadState(
                    loadState,
                    redditPageAdapter.itemCount,
                    1,
                    progressbarRedditPage,
                    recyclerviewPosts,
                    buttonRedditPageRetry,
                    textviewRedditPageError,
                    textviewRedditPageEmpty,
                    refreshRedditPage
                )
            }
        }

        setHasOptionsMenu(true)
    }


    //inflate/activate options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_reddit_page, menu)
        viewLifecycleOwner.lifecycleScope.launch {
            var isChecked = viewModel.preferencesFlow.first().isCompactView
            menu.findItem(R.id.action_compact).isChecked = isChecked
            viewModel.checkIsCompact(isChecked)

        }

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //when statement for each of the menu items
        return when(item.itemId) {
            R.id.action_compact -> {
                val newVal = !item.isChecked
                item.isChecked = newVal //set to opposite selected
                viewModel.onCompactViewClicked(newVal)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSearchSubmit(query: String?, subredditName: String) {
        if (query != null && query != "") {
            //TODO emit these from viewmodel
            findNavController().navigate(
                RedditPageFragmentDirections.actionRedditPageFragmentToSearchResultsFragment(
                    query,
                    subredditName
                )
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(post: RedditChildrenObject) {
        //findNavController().navigate(RedditPageFragmentDirections.actionRedditPageFragmentToPostDetailFragment(post))
    }


    //type will tell us what the user has clicked on item
    //for example: if user clicked on the upvote button, the type will be 1
    override fun onVoteClick(post: RedditChildrenObject, type: Int) {
        //pass the post object to the post details screen
        when (type) {
            -1 -> viewModel.onVoteOnPost(-1, post)
            0 -> viewModel.onVoteOnPost(0, post)
            1 -> viewModel.onVoteOnPost(1, post)
        }
    }

}