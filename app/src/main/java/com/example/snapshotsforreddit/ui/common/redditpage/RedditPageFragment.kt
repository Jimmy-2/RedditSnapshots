package com.example.snapshotsforreddit.ui.common.redditpage

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentRedditPageBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.ui.common.loadstate.RedditLoadStateAdapter
import com.example.snapshotsforreddit.util.changeViewOnLoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RedditPageFragment : Fragment(R.layout.fragment_reddit_page),
    RedditPageAdapterTest.OnItemClickListener {
    private val navigationArgs: RedditPageFragmentArgs by navArgs()
    private val viewModel: RedditPageViewModel by viewModels()

    private var _binding: FragmentRedditPageBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRedditPageBinding.bind(view)

        val redditPageAdapter = RedditPageAdapterTest(this)

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
        //only load this once
        viewModel.loadRedditPage(redditPageName, redditPageType)

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
        //reference to SearchView
        val searchPost = menu.findItem(R.id.action_search_subreddits)

        viewModel.subredditName.observe(viewLifecycleOwner) {
            searchPost.title = (it + downArrow).replaceFirstChar { it.uppercase() }
        }


        val searchView = searchPost.actionView as SearchView
        searchView.queryHint = "Subreddit..."
        //REFORMAT AND USE VIEW EXTENSIONS

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.changeRedditPage(query, "r")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //TODO display a list of subreddits that start with the searched query and update as the user deletes/adds a letter.
                //TODO: MAY NOT ADD DUE TO 60 API CALLS PER MIN LIMIT AND THIS CAN EASILY REACH LIMIT
                return true
            }
        })



        viewLifecycleOwner.lifecycleScope.launch {
            val isChecked = viewModel.preferencesFlow.first().isCompactView
            menu.findItem(R.id.action_compact).isChecked = isChecked
            viewModel.checkIsCompact(isChecked)

        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //when statement for each of the menu items
        return when (item.itemId) {
            R.id.action_compact -> {
                val newVal = !item.isChecked
                item.isChecked = newVal //set to opposite selected
                viewModel.onCompactViewClicked(newVal)
                return true
            }
            //TODO add ischecked to the menu items
            R.id.action_sort_by_best -> {
                viewModel.onSortOrderSelected("best")
                return true
            }
            R.id.action_sort_by_hot -> {
                viewModel.onSortOrderSelected("hot")
                return true
            }
            R.id.action_sort_by_new -> {
                viewModel.onSortOrderSelected("new")
                return true
            }
            R.id.action_sort_by_rising -> {
                viewModel.onSortOrderSelected("rising")
                return true
            }
            R.id.action_sort_by_top -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSearchSubmit(query: String?, subredditName: String) {
        if (query != null && query != "") {
            //TODO emit these from viewmodel
            findNavController().navigate(
                RedditPageFragmentDirections.actionRedditPageFragmentToSearchResultsPostFragment(
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

//    override fun onItemClick(post: RedditChildrenObject) {
//        //findNavController().navigate(RedditPageFragmentDirections.actionRedditPageFragmentToPostDetailFragment(post))
//    }


    //type will tell us what the user has clicked on item
    //for example: if user clicked on the upvote button, the type will be 1
//    override fun onVoteClick(post: RedditChildrenObject, type: Int) {
//        //pass the post object to the post details screen
//        when (type) {
//            -1 -> viewModel.onVoteOnPost(-1, post)
//            0 -> viewModel.onVoteOnPost(0, post)
//            1 -> viewModel.onVoteOnPost(1, post)
//        }
//    }


    override fun onItemClick(post: RedditChildrenData) {
        //findNavController().navigate(RedditPageFragmentDirections.actionRedditPageFragmentToPostDetailFragment(post))
    }

    override fun onVoteClick(post: RedditChildrenData, type: Int) {
        //pass the post object to the post details screen
        when (type) {
            -1 -> viewModel.onVoteOnPost(-1, post)
            0 -> viewModel.onVoteOnPost(0, post)
            1 -> viewModel.onVoteOnPost(1, post)
        }
    }


    companion object {
        const val downArrow = 0x25BC.toChar()
    }

}