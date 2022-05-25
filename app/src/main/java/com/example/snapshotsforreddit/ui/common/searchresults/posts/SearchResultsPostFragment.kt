package com.example.snapshotsforreddit.ui.common.searchresults.posts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentSearchResultsPostBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.util.changeViewOnLoadState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

//TODO change to RedditChildrenData (not object)
class SearchResultsPostFragment : Fragment(R.layout.fragment_search_results_post),
    SearchResultsPostAdapter.OnItemClickListener {
    private val navigationArgs: SearchResultsPostFragmentArgs by navArgs()
    private val viewModel: SearchResultsViewModel by viewModels()

    private var _binding: FragmentSearchResultsPostBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentSearchResultsPostBinding.bind(view)

        val searchResultsAdapter = SearchResultsPostAdapter(this)

        binding.apply {
            recyclerviewSearchResults.setHasFixedSize(true)
            recyclerviewSearchResults.adapter = searchResultsAdapter

            refreshSearchResults.setOnRefreshListener { searchResultsAdapter.refresh() }
            buttonSearchResultsRetry.setOnClickListener { searchResultsAdapter.retry() }
        }

        val currentSearchQuery = CurrentSearch(navigationArgs.searchQuery,navigationArgs.subredditName)
        viewModel.changeQuery(currentSearchQuery )

        viewModel.preferencesFlow.observe(viewLifecycleOwner) { preferencesFlowValues ->
            viewModel.checkIsCompact(preferencesFlowValues.isCompactView)
        }

        viewModel.searchResults.observe(viewLifecycleOwner) {
            //connect data to adapter
            searchResultsAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
        searchResultsAdapter.addLoadStateListener { loadState ->
            binding.apply {
                changeViewOnLoadState(
                    loadState,
                    searchResultsAdapter.itemCount,
                    0,
                    progressbarSearchResults,
                    recyclerviewSearchResults,
                    buttonSearchResultsRetry,
                    textviewSearchResultsError,
                    textviewSearchResultsEmpty,
                    refreshSearchResults
                )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(post: RedditChildrenData) {
        TODO("Not yet implemented")
    }

    override fun onVoteClick(post: RedditChildrenData, type: Int) {
        TODO("Not yet implemented")
    }


}