package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.search.subreddits

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jimmywu.snapshotsforreddit.R
import com.jimmywu.snapshotsforreddit.databinding.FragmentSearchResultsSubredditBinding
import com.jimmywu.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData
import com.jimmywu.snapshotsforreddit.util.changeViewOnLoadState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsSubredditFragment : Fragment(R.layout.fragment_search_results_subreddit),
    SearchResultsSubredditAdapter.OnItemClickListener  {
    private val navigationArgs: SearchResultsSubredditFragmentArgs by navArgs()
    private val viewModel: SearchResultsSubredditViewModel by viewModels()

    private var _binding: FragmentSearchResultsSubredditBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentSearchResultsSubredditBinding.bind(view)

        val searchResultsSubredditAdapter = SearchResultsSubredditAdapter(this)

        binding.apply {
            recyclerviewSearchResultsSubreddit.setHasFixedSize(true)
            recyclerviewSearchResultsSubreddit.adapter = searchResultsSubredditAdapter
            refreshSearchResultsSubreddit.setOnRefreshListener { searchResultsSubredditAdapter.refresh() }
            buttonSearchResultsSubredditRetry.setOnClickListener { searchResultsSubredditAdapter.retry() }
        }

        val currentSubredditSearchQuery = navigationArgs.searchQuery
        viewModel.changeQuery(currentSubredditSearchQuery)

        viewModel.searchResults.observe(viewLifecycleOwner) {
            //connect data to adapter
            searchResultsSubredditAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
        searchResultsSubredditAdapter.addLoadStateListener { loadState ->
            binding.apply {
                changeViewOnLoadState(
                    loadState,
                    searchResultsSubredditAdapter.itemCount,
                    0,
                    progressbarSearchResultsSubreddit,
                    recyclerviewSearchResultsSubreddit,
                    buttonSearchResultsSubredditRetry,
                    textviewSearchResultsSubredditError,
                    textviewSearchResultsSubredditEmpty,
                    refreshSearchResultsSubreddit
                )

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(subreddit: SubredditChildrenData) {
        //TODO PUT LOGIC INTO VIEWMODEL NOT FRAGMENT
        if(subreddit.display_name_prefixed != null) {
            val action = when {
                subreddit.display_name_prefixed == "Home" -> {
                    SearchResultsSubredditFragmentDirections.actionSearchResultsSubredditFragmentToRedditPageFragment2(
                        "", "", false
                    )
                }
                subreddit.subreddit_type == "user" -> {
                    SearchResultsSubredditFragmentDirections.actionSearchResultsSubredditFragmentToRedditPageFragment2(
                        subreddit.display_name_prefixed.substring(2), "user", false
                    )
                }
                else -> {
                    SearchResultsSubredditFragmentDirections.actionSearchResultsSubredditFragmentToRedditPageFragment2(
                        subreddit.display_name_prefixed.substring(2), "r", false)
                }
            }
            findNavController().navigate(action)
        }
    }
}