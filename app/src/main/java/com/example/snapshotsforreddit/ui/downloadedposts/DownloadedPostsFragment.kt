package com.example.snapshotsforreddit.ui.downloadedposts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snapshotsforreddit.BaseApplication
import com.example.snapshotsforreddit.adapter.DownloadedPostsAdapter
import com.example.snapshotsforreddit.databinding.FragmentDownloadedPostsBinding



//will create a bottom tab navigation that seperates saved posts fragment from the fragments that require api usage
class DownloadedPostsFragment : Fragment() {
    private val viewModel: DownloadedPostsViewModel by activityViewModels {
        DownloadedPostsViewModelFactory(
            (activity?.application as BaseApplication).database.postDao()
        )
    }
    private var _binding: FragmentDownloadedPostsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadedPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DownloadedPostsAdapter {
            val action = DownloadedPostsFragmentDirections.actionDownloadedPostsFragmentToPostDetailFragment(it.permalink, "HELLO")
            this.findNavController().navigate(action)
        }


        binding.downloadedList.layoutManager = LinearLayoutManager(this.context)
        binding.downloadedList.adapter = adapter


        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }


    }

}