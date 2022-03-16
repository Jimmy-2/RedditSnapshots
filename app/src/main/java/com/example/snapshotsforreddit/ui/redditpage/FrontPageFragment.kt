package com.example.snapshotsforreddit.ui.redditpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.snapshotsforreddit.adapter.FrontPageAdapter
import com.example.snapshotsforreddit.databinding.FragmentFrontPageBinding
import com.example.snapshotsforreddit.model.RedditViewModel


class FrontPageFragment : Fragment() {
    private val viewModel: RedditViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFrontPageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.postsList.adapter = FrontPageAdapter()
        //viewModel.getPosts()
        return binding.root
    }
}