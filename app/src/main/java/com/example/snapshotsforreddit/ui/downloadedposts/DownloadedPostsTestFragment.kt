package com.example.snapshotsforreddit.ui.downloadedposts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentDownloadedPostsTestBinding
import dagger.hilt.android.AndroidEntryPoint

//comments will be its own database.
//literalkly down every single comment and then sort then based on their
//jsonobjects
@AndroidEntryPoint
class DownloadedPostsTestFragment: Fragment(R.layout.fragment_downloaded_posts_test) {
    private val viewModel: DownloadedPostsTestViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //already inflated in constructor. We could inflate it in oncreateview this way:
        /*
        _binding = FragmentDownloadedPostsBinding.inflate(inflater, container, false)
        return binding.root
         */
        val binding = FragmentDownloadedPostsTestBinding.bind(view)

        val downloadedPostsTestAdapter = DownloadedPostsTestAdapter()

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
    }

}