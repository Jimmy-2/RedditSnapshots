package com.example.snapshotsforreddit.ui.tabs.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentSubscribedBinding
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenObject
import com.example.snapshotsforreddit.ui.general.RedditLoadStateAdapter
import com.example.snapshotsforreddit.util.changeViewOnLoadState
import dagger.hilt.android.AndroidEntryPoint


//TODO: TURN THIS TO SIDE SHEET ON LEFT SIDE

@AndroidEntryPoint
class SubscribedFragment : Fragment(R.layout.fragment_subscribed), SubscribedAdapter.OnItemClickListener{
    private val viewModel: SubscribedViewModel by viewModels()

    private var _binding: FragmentSubscribedBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentSubscribedBinding.bind(view)

        val subscribedAdapter = SubscribedAdapter(this)

        binding.apply {
            recyclerviewSubreddits.itemAnimator = null
            recyclerviewSubreddits.setHasFixedSize(true)
            recyclerviewSubreddits.adapter = subscribedAdapter.withLoadStateHeaderAndFooter(
                header = RedditLoadStateAdapter {subscribedAdapter .retry()},
                footer = RedditLoadStateAdapter {subscribedAdapter .retry()}
            )
            refreshSubscribed.setOnRefreshListener { subscribedAdapter.refresh() }
            buttonSubscribedRetry.setOnClickListener { subscribedAdapter.retry() }
        }


        //whenever authFlow is changed (getting new accesstoken/refreshtoken), we will refresh the subscribed subreddits list
        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.checkIfAccessTokenChanged(authFlowValues.accessToken)
        }


        //whenever the subscribed subreddits list is changed, we will refresh the recyclerview
        viewModel.subreddits.observe(viewLifecycleOwner) {
            //connect data to adapter
            subscribedAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }



        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
        subscribedAdapter.addLoadStateListener { loadState ->
            binding.apply {
                changeViewOnLoadState(
                    loadState,
                    subscribedAdapter.itemCount,
                    0,
                    progressbarSubscribed,
                    recyclerviewSubreddits,
                    buttonSubscribedRetry,
                    textviewSubscribedError,
                    textviewSubscribedEmpty,
                    refreshSubscribed
                )
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(subreddit: SubredditChildrenObject) {
        //TODO PUT LOGIC INTO VIEWMODEL NOT FRAGMENT
        if(subreddit.data?.display_name_prefixed != null) {
            val action = when {
                subreddit.data.display_name_prefixed == "Home" -> {
                    SubscribedFragmentDirections.actionSubscribedFragmentToRedditPageFragment(
                        "", ""
                    )
                }
                subreddit.data.subreddit_type == "user" -> {
                    SubscribedFragmentDirections.actionSubscribedFragmentToRedditPageFragment(
                        subreddit.data.display_name_prefixed.substring(2), "user"
                    )
                }
                else -> {
                    SubscribedFragmentDirections.actionSubscribedFragmentToRedditPageFragment(
                        subreddit.data.display_name_prefixed.substring(2), "r")
                }
            }
            findNavController().navigate(action)
        }

    }

}


