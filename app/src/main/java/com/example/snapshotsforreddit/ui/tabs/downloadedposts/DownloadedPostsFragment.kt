package com.example.snapshotsforreddit.ui.tabs.downloadedposts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.data.Repository.SortOrder
import com.example.snapshotsforreddit.data.Room.Post
import com.example.snapshotsforreddit.databinding.FragmentDownloadedPostsBinding
import com.example.snapshotsforreddit.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//comments will be its own database.
//literalkly down every single comment and then sort then based on their
//jsonobjects
@AndroidEntryPoint
class DownloadedPostsFragment: Fragment(R.layout.fragment_downloaded_posts), DownloadedPostsAdapter.OnItemClickListener {
    private val viewModel: DownloadedPostsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //already inflated in constructor. We could inflate it in oncreateview this way:
        /*
        _binding = FragmentDownloadedPostsBinding.inflate(inflater, container, false)
        return binding.root
         */
        val binding = FragmentDownloadedPostsBinding.bind(view)

        val downloadedPostsAdapter = DownloadedPostsAdapter(this)

        binding.apply {
            recyclerViewPosts.apply {
                adapter = downloadedPostsAdapter
                layoutManager = LinearLayoutManager(this.context)
                setHasFixedSize(true) // optimization for recyclerview if it doesnt change size in screen
            }//basically same as
            // binding.downloadedList.adapter = downloadedPostsTestAdapter
            // binding.downloadedList.layoutManager = LinearLayoutManager(this.context)


            //swipe to delete
            //allow swipe directions both ways
            ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    //up/down drag/down not needed
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //get current post item
                    val post = downloadedPostsAdapter.currentList[viewHolder.adapterPosition]

                    viewModel.onPostSwiped(post)

                }

            }).attachToRecyclerView(recyclerViewPosts)



        }
        //viewLifecycleOwner so the viewmodel knows when to stop updating, such as when fragment is destroyed
        viewModel.downloadedPosts.observe(viewLifecycleOwner) {
            //lambda function tells us what to do when changes/updates occur
            //we can decide what to do such as updating the adapter
            downloadedPostsAdapter.submitList(it)
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