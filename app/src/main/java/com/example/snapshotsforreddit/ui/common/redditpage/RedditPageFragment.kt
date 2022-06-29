package com.example.snapshotsforreddit.ui.common.redditpage


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentRedditPageBinding
import com.example.snapshotsforreddit.ui.common.loadstate.RedditLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RedditPageFragment : Fragment(R.layout.fragment_reddit_page){
    private val navigationArgs: RedditPageFragmentArgs by navArgs()
    private val viewModel: RedditPageViewModel by viewModels()

    private var _binding: FragmentRedditPageBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRedditPageBinding.bind(view)

//        val redditPageAdapter = RedditPageAdapter(this)
//
//        binding.apply {
//            recyclerviewPosts.setHasFixedSize(true)
//            recyclerviewPosts.itemAnimator?.changeDuration = 0
//            recyclerviewPosts.adapter = redditPageAdapter.withLoadStateHeaderAndFooter(
//                header = RedditLoadStateAdapter { redditPageAdapter.retry() },
//                footer = RedditLoadStateAdapter { redditPageAdapter.retry() }
//            )
//            refreshRedditPage.setOnRefreshListener { redditPageAdapter.refresh() }
//            buttonRedditPageRetry.setOnClickListener { redditPageAdapter.retry() }
//        }
//
//
//        val redditPageName = navigationArgs.redditPageName
//        val redditPageType = navigationArgs.redditPageType
//        val isDefaults = navigationArgs.isDefaults
//        //only load this once
//        viewModel.loadRedditPage(redditPageName, redditPageType, isDefaults)
//
//        viewModel.redditPagePosts.observe(viewLifecycleOwner) {
//            //connect data to adapter
//            redditPageAdapter.submitData(viewLifecycleOwner.lifecycle, it)
//
//        }
//
//        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
//        redditPageAdapter.addLoadStateListener { loadState: CombinedLoadStates ->
//            binding.apply {
//                changeViewOnLoadState(
//                    loadState,
//                    redditPageAdapter.itemCount,
//                    1,
//                    progressbarRedditPage,
//                    recyclerviewPosts,
//                    buttonRedditPageRetry,
//                    textviewRedditPageError,
//                    textviewRedditPageEmpty,
//                    refreshRedditPage
//                )
//            }
//        }

        val redditPageName = navigationArgs.redditPageName
        val redditPageType = navigationArgs.redditPageType
        val isDefaults = navigationArgs.isDefaults
        //only load this once

        viewModel.onRedditPageLoad(redditPageName)

//        viewModel.loadRedditPage(redditPageName, redditPageType, isDefaults)

        val redditPageAdapter = RedditPagePagingAdapter(
            onItemClick = { redditPagePost->

            },
            onVoteClick = {redditPagePost, isUpvote ->
                viewModel.onVoteClick(redditPagePost, isUpvote)
            },
            onMoreClick = {redditPagePost, voteType->
                findNavController().navigate(RedditPageFragmentDirections.actionRedditPageFragmentRPToMoreOptionsDialogFragmentRP())
            },
            onSubredditClick = {subredditName ->
                if(subredditName != null) {
                    findNavController().navigate(RedditPageFragmentDirections.actionRedditPageFragmentRPSelf(subredditName, "r", false))

                }

            }

        )

        binding.apply {
            recyclerviewPosts.apply {
                adapter = redditPageAdapter.withLoadStateFooter(
                    RedditLoadStateAdapter(redditPageAdapter::retry)
                )
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                itemAnimator?.changeDuration = 0
            }



            viewLifecycleOwner.lifecycleScope.launchWhenStarted{
                viewModel.redditPagePostsTest2.collectLatest {

//                    textViewInstructions.isVisible = false
                    redditPageAdapter.submitData(it)
                }

            }


            //                    redditPageAdapter.itemCount,
//                    1,
//                    progressbarRedditPage,
//                    recyclerviewPosts,
//                    buttonRedditPageRetry,
//                    textviewRedditPageError,
//                    textviewRedditPageEmpty,
//                    refreshRedditPage


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                redditPageAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.source.refresh }
                    .filter { it.source.refresh is LoadState.NotLoading }
                    .collect {
                        if (viewModel.pendingScrollToTopAfterNewQuery) {
                            recyclerviewPosts.scrollToPosition(0)
                            viewModel.pendingScrollToTopAfterNewQuery = false
                        }
                        if (viewModel.pendingScrollToTopAfterRefresh && it.mediator?.refresh is LoadState.NotLoading) {
                            recyclerviewPosts.scrollToPosition(0)
                            viewModel.pendingScrollToTopAfterRefresh = false
                        }
                    }
            }

//            lifecycleScope.launchWhenCreated {
//                redditPageAdapter.loadStateFlow
//                    // Use a state-machine to track LoadStates such that we only transition to
//                    // NotLoading from a RemoteMediator load if it was also presented to UI.
//                    .asMergedLoadStates()
//                    // Only emit when REFRESH changes, as we only want to react on loads replacing the
//                    // list.
//                    .distinctUntilChangedBy { it.refresh }
//                    // Only react to cases where REFRESH completes i.e., NotLoading.
//                    .filter { it.refresh is LoadState.NotLoading }
//                    // Scroll to top is synchronous with UI updates, even if remote load was triggered.
//                    .collect { binding.recyclerviewPosts.scrollToPosition(0) }
//            }


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                redditPageAdapter.loadStateFlow
                    .collect { loadState ->
                        when (val refresh = loadState.mediator?.refresh) {
                            is LoadState.Loading -> {
                                textviewRedditPageError.isVisible = false
                                buttonRedditPageRetry.isVisible = false
                                refreshRedditPage.isRefreshing = true
//                                textViewNoResults.isVisible = false
                                recyclerviewPosts.isVisible = redditPageAdapter.itemCount > 0
                            }
                            is LoadState.NotLoading -> {
                                textviewRedditPageError.isVisible = false
                                buttonRedditPageRetry.isVisible = false
                                refreshRedditPage.isRefreshing = false
                                recyclerviewPosts.isVisible = redditPageAdapter.itemCount > 0

                                val noResults =
                                    redditPageAdapter.itemCount < 1 && loadState.append.endOfPaginationReached
                                            && loadState.source.append.endOfPaginationReached

//                                textViewNoResults.isVisible = noResults
                            }
                            is LoadState.Error -> {
                                refreshRedditPage.isRefreshing = false
//                                textViewNoResults.isVisible = false
                                recyclerviewPosts.isVisible = redditPageAdapter.itemCount > 0

                                val noCachedResults =
                                    redditPageAdapter.itemCount < 1 && loadState.source.append.endOfPaginationReached

                                textviewRedditPageError.isVisible = noCachedResults
                                buttonRedditPageRetry.isVisible = noCachedResults

//                                val errorMessage = getString(
//                                    R.string.could_not_load_search_results,
//                                    refresh.error.localizedMessage
//                                        ?: getString(R.string.unknown_error_occurred)
//                                )
//                                textViewError.text = errorMessage
                            }
                        }
                    }
            }

            refreshRedditPage.setOnRefreshListener { redditPageAdapter.refresh()
//                recyclerviewPosts.scrollToPosition(0)
            }

            buttonRedditPageRetry.setOnClickListener { redditPageAdapter.retry()
//                recyclerviewPosts.scrollToPosition(0)
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()


    }

//    override fun onBottomNavigationFragmentReselected() {
//        binding.recyclerviewPosts.scrollToPosition(0)
//    }

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

            //TODO BOTTOM SHEET DIALOG FOR OPTIONS SELECTED

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

//    override fun onSearchSubmit(query: String?, subredditName: String) {
//        if (query != null && query != "") {
//            //TODO emit these from viewmodel
//            findNavController().navigate(
//                RedditPageFragmentDirections.actionRedditPageFragmentRPToSearchResultsPostFragmentRP(
//                    query,
//                    subredditName
//                )
//            )
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("HELLO123 DESTROYING")
        binding.recyclerviewPosts.adapter = null

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




//    override fun onVoteClick(post: RedditChildrenData, type: Int) {
//        //pass the post object to the post details screen
//        when (type) {
//            -1 -> viewModel.onVoteOnPost(-1, post)
//            0 -> viewModel.onVoteOnPost(0, post)
//            1 -> viewModel.onVoteOnPost(1, post)
//        }
//    }




    companion object {
        const val downArrow = 0x25BC.toChar()
    }

}