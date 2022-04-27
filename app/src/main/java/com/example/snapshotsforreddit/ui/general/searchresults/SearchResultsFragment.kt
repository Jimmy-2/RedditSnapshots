package com.example.snapshotsforreddit.ui.general.searchresults

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentSearchResultsBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.util.changeViewOnLoadState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

//TODO change to RedditChildrenData (not object)
class SearchResultsFragment : Fragment(R.layout.fragment_search_results), SearchResultsAdapter.OnItemClickListener  {
    private val navigationArgs: SearchResultsFragmentArgs by navArgs()
    private val viewModel: SearchResultsViewModel by viewModels()

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentSearchResultsBinding.bind(view)

        val searchResultsAdapter = SearchResultsAdapter(this)

        binding.apply {
            recyclerviewSearchResults.setHasFixedSize(true)
            recyclerviewSearchResults.adapter = searchResultsAdapter

            refreshSearchResults.setOnRefreshListener { searchResultsAdapter.refresh() }
            buttonSearchResultsRetry.setOnClickListener { searchResultsAdapter.retry() }
        }

        val currentSearchQuery = CurrentSearch(navigationArgs.searchQuery,navigationArgs.subredditName)
        viewModel.changeQuery(currentSearchQuery )


        viewModel.searchResults.observe(viewLifecycleOwner) {
            //connect data to adapter
            searchResultsAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
        searchResultsAdapter.addLoadStateListener { loadState ->
            binding.apply {
                changeViewOnLoadState(loadState, searchResultsAdapter.itemCount, 0 , progressbarSearchResults, recyclerviewSearchResults, buttonSearchResultsRetry, textviewSearchResultsError, textviewSearchResultsEmpty, refreshSearchResults)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(post: RedditChildrenObject) {

    }

    override fun onVoteClick(post: RedditChildrenObject, type: Int, position: Int) {

    }

}