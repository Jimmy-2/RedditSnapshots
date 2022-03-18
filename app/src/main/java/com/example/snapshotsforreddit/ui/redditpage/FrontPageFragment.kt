package com.example.snapshotsforreddit.ui.redditpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.adapter.FrontPageAdapter
import com.example.snapshotsforreddit.adapter.FrontPageListener
import com.example.snapshotsforreddit.databinding.FragmentFrontPageBinding
import com.example.snapshotsforreddit.model.RedditViewModel
import com.example.snapshotsforreddit.network.responses.ChildrenData


class FrontPageFragment : Fragment() {
    private val sharedViewModel: RedditViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFrontPageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = sharedViewModel
        binding.postsList.adapter = FrontPageAdapter(FrontPageListener {
            findNavController().navigate(R.id.action_frontPageFragment_to_postDetailFragment)

        })

        //viewModel.getPosts()
        return binding.root
    }
}