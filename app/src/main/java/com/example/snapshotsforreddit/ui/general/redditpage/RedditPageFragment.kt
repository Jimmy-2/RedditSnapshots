package com.example.snapshotsforreddit.ui.general.redditpage

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentRedditPageBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RedditPageFragment: Fragment(R.layout.fragment_reddit_page), RedditPageAdapter.OnItemClickListener {
    private val navigationArgs: RedditPageFragmentArgs by navArgs()
    private val viewModel: RedditPageViewModel by viewModels()

    private var _binding: FragmentRedditPageBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentRedditPageBinding.bind(view)

        val redditPageAdapter = RedditPageAdapter(this)

        binding.apply {
            recyclerviewPosts.setHasFixedSize(true)
            recyclerviewPosts.adapter = redditPageAdapter


        }

        val redditPageName= navigationArgs.redditPageName
        val redditPageType = navigationArgs.redditPageType
        viewModel.redditPageInformation(redditPageName, redditPageType)

        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.checkIfAccessTokenChanged(authFlowValues.accessToken)
        }

        viewModel.redditPagePosts.observe(viewLifecycleOwner) {
            //connect data to adapter
            redditPageAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        setHasOptionsMenu(true)
    }

    //inflate/activate options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_reddit_page, menu)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onItemClick(post: RedditChildrenObject) {
        findNavController().navigate(RedditPageFragmentDirections.actionRedditPageFragmentToPostDetailFragment(post))
    }


    //type will tell us what the user has clicked on item
    //for example: if user clicked on the upvote button, the type will be 1
    override fun onVoteClick(post: RedditChildrenObject, type: Int, position: Int) {
        //pass the post object to the post details screen
        when (type) {
            -1 -> viewModel.voteOnPost(-1, post)
            0 -> viewModel.voteOnPost(0, post)
            1 -> viewModel.voteOnPost(1, post)
        }
    }

}