package com.example.snapshotsforreddit.ui.tabs.search

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentSearchBinding.bind(view)



        binding.apply {
            buttonSearchPost.setOnClickListener {
                searchNavigate(viewModel.searchQuery.value, null)
            }
            buttonSearchSubreddit.setOnClickListener {
                searchNavigate(viewModel.searchQuery.value, 0)
            }
            buttonSearchUser.setOnClickListener {
                searchNavigate(viewModel.searchQuery.value, 1)
            }

            searchViewSearchTab.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if(query != null && query != "") {
                        searchNavigate(query, null)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null && newText != "") {
                        viewModel.searchQuery.value = newText

                        searchButtons.visibility= View.VISIBLE

                        textviewSearchPost.text = "Posts with \"${viewModel.searchQuery.value}\""

                        textviewSearchSubreddit.text = "Subreddits with \"${viewModel.searchQuery.value}\""

                        textviewSearchUser.text = "Go to User \"${viewModel.searchQuery.value}\""
                    }else {
                        searchButtons.visibility=GONE

                    }
                    return true
                }

            })

        }
    }

    //TODO put logic in viewmodel and emit to fragment
    fun searchNavigate(searchQuery: String, searchType: Int?) {
        when(searchType) {
            0 -> findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultsSubredditFragment(searchQuery))
            1 -> findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToUserOverviewFragment(searchQuery))
            else  -> findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultsPostFragment2(searchQuery, null))
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}