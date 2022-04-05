package com.example.snapshotsforreddit.ui.general.redditpage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentRedditPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RedditPageFragment: Fragment(R.layout.fragment_reddit_page) {
    private val navigationArgs: RedditPageFragmentArgs by navArgs()
    private val viewModel: RedditPageViewModel by viewModels()

    private var _binding: FragmentRedditPageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentRedditPageBinding.bind(view)

        val subredditAdapter = RedditPageAdapter()

        binding.apply {
            recyclerviewPosts.setHasFixedSize(true)
            recyclerviewPosts.adapter = subredditAdapter


        }

        val redditPageName= navigationArgs.redditPageName
        val redditPageType = navigationArgs.redditPageType
        viewModel.redditPageInformation(redditPageName, redditPageType)

        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.checkIfAccessTokenChanged(authFlowValues.accessToken)
        }

        viewModel.redditPagePosts.observe(viewLifecycleOwner) {
            //connect data to adapter
            subredditAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}