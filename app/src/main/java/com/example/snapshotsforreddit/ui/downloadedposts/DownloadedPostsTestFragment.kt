package com.example.snapshotsforreddit.ui.downloadedposts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.data.SortOrder
import com.example.snapshotsforreddit.data.room.Post
import com.example.snapshotsforreddit.databinding.FragmentDownloadedPostsTestBinding
import com.example.snapshotsforreddit.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//comments will be its own database.
//literalkly down every single comment and then sort then based on their
//jsonobjects
@AndroidEntryPoint
class DownloadedPostsTestFragment: Fragment(R.layout.fragment_downloaded_posts_test), DownloadedPostsTestAdapter.OnItemClickListener {
    private val viewModel: DownloadedPostsTestViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //already inflated in constructor. We could inflate it in oncreateview this way:
        /*
        _binding = FragmentDownloadedPostsBinding.inflate(inflater, container, false)
        return binding.root
         */
        val binding = FragmentDownloadedPostsTestBinding.bind(view)

        val downloadedPostsTestAdapter = DownloadedPostsTestAdapter(this)

        binding.apply {
            recyclerViewPosts.apply {
                adapter = downloadedPostsTestAdapter
                layoutManager = LinearLayoutManager(this.context)
                setHasFixedSize(true) // optimization for recyclerview if it doesnt change size in screen
            }
        }//basically same as
        // binding.downloadedList.adapter = downloadedPostsTestAdapter
        // binding.downloadedList.layoutManager = LinearLayoutManager(this.context)

        //viewLifecycleOwner so the viewmodel knows when to stop updating, such as when fragment is destroyed
        viewModel.downloadedPosts.observe(viewLifecycleOwner) {
            //lambda function tells us what to do when changes/updates occur
            //we can decide what to do such as updating the adapter
            downloadedPostsTestAdapter.submitList(it)
            //diffutil will do the other logic such as calcualting the differences, etc
        }

        setHasOptionsMenu(true)
    }

    //go to details screen on item clicked
    override fun onItemClick(post: Post) {
        viewModel.onPostSelected(post)
    }


    //inflate/activate options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_downloaded_posts, menu)

        //reference to SearchView
        val searchPost = menu.findItem(R.id.action_search_downloaded)
        val searchView = searchPost.actionView as SearchView

        searchView.onQueryTextChanged {
            //it is whatever we pass to the listener in the extension function

            //update search query
            viewModel.searchQuery.value = it

        }
        //lives as long as fragment view lives
        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_compact).isChecked =
                    //read value from flow with collect or first since we do not need to check again
                    //when app starts, we get isCompactView value from preferencesFlow and then set it to menu item and cancels the flow inside this coroutine.
                viewModel.preferencesFlow.first().isCompactView
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //when statement for each of the menu items
        return when(item.itemId) {
            R.id.action_sort_by_title -> {
                viewModel.onSortOrderSelected(SortOrder.BY_TITLE)
                return true
            }

            R.id.action_sort_by_date_added -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                return true
            }
            R.id.action_sort_by_subreddit -> {
                viewModel.onSortOrderSelected(SortOrder.BY_SUBREDDIT)
                return true
            }
            R.id.action_compact -> {
                item.isChecked = !item.isChecked //set to opposite selected
                viewModel.onCompactViewClicked(item.isChecked)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}