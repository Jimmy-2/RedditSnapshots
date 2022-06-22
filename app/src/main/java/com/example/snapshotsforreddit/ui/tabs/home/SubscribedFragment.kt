package com.example.snapshotsforreddit.ui.tabs.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentSubscribedBinding
import com.example.snapshotsforreddit.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


//TODO: TURN THIS TO SIDE SHEET ON LEFT SIDE

@AndroidEntryPoint
class SubscribedFragment : Fragment(R.layout.fragment_subscribed) {
    private val viewModel: SubscribedViewModel by viewModels()

    private var _binding: FragmentSubscribedBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSubscribedBinding.bind(view)

//        val subscribedAdapter = SubscribedAdapter(this)
//
//        binding.apply {
//            recyclerviewSubreddits.itemAnimator = null
//            recyclerviewSubreddits.setHasFixedSize(true)
//            recyclerviewSubreddits.adapter = subscribedAdapter.withLoadStateHeaderAndFooter(
//                header = RedditLoadStateAdapter {subscribedAdapter .retry()},
//                footer = RedditLoadStateAdapter {subscribedAdapter .retry()}
//            )
//            refreshSubscribed.setOnRefreshListener {
//                subscribedAdapter.refresh()
////                viewModel.recursion()
//            }
//            buttonSubscribedRetry.setOnClickListener { subscribedAdapter.retry() }
//        }
//
//
//        //whenever authFlow is changed (getting new accesstoken/refreshtoken), we will refresh the subscribed subreddits list
//        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
//            viewModel.checkIfAccessTokenChanged(authFlowValues.accessToken)
//        }
//
//
//        //whenever the subscribed subreddits list is changed, we will refresh the recyclerview
//        viewModel.subreddits.observe(viewLifecycleOwner) {
//            //connect data to adapter
//            subscribedAdapter.submitData(viewLifecycleOwner.lifecycle, it)
//
//        }
//
//
//
//        //depending on the load state of the adapter (list of items) (error, loading, no results), we will display the necessary view for the user to see
//        subscribedAdapter.addLoadStateListener { loadState ->
//            binding.apply {
//                changeViewOnLoadState(
//                    loadState,
//                    subscribedAdapter.itemCount,
//                    0,
//                    progressbarSubscribed,
//                    recyclerviewSubreddits,
//                    buttonSubscribedRetry,
//                    textviewSubscribedError,
//                    textviewSubscribedEmpty,
//                    refreshSubscribed
//                )
//            }
//        }

        val subscribedAdapter = SubscribedSubredditAdapter(
            onSubscribedClick = {subscribedSubreddit ->
                if(subscribedSubreddit.display_name_prefixed != null) {
                    val action = when (subscribedSubreddit.subreddit_type) {
                        "user" -> {
                            SubscribedFragmentDirections.actionSubscribedFragmentToRedditpageNavigation(
                                subscribedSubreddit.display_name_prefixed.substring(2), "user" , false
                            )
                        }
                        else -> {
                            SubscribedFragmentDirections.actionSubscribedFragmentToRedditpageNavigation(
                                subscribedSubreddit.display_name_prefixed.substring(2), "r", false)
                        }
                    }
                    findNavController().navigate(action)
        }

            },
            onDefaultClick = { defaultName ->
                val action =
                    SubscribedFragmentDirections.actionSubscribedFragmentToRedditpageNavigation(
                        defaultName, "r", true
                    )

                findNavController().navigate(action)
            }
        )

        binding.apply {
            recyclerviewSubreddits.apply {
                adapter = subscribedAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }


            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.subscribedSubreddits.collect {
                    val results = it ?: return@collect

                    //TODO TEST RELOADING WITH NO INTERNET
                    //TODO results keeps on loading and is unstoppable. fix this bug
                    refreshSubscribed.isRefreshing = results is Resource.Loading

                    recyclerviewSubreddits.isVisible = !results.data.isNullOrEmpty()


                    //TODO removed try catch in order to show error. Change this at end
                    textviewSubscribedError.isVisible =
                        results.error != null && results.data.isNullOrEmpty()
                    buttonSubscribedRetry.isVisible =
                        results.error != null && results.data.isNullOrEmpty()

                    subscribedAdapter.submitList(results.data)

                }
            }

            refreshSubscribed.setOnRefreshListener {
                viewModel.onRefresh()
            }
            buttonSubscribedRetry.setOnClickListener {
                viewModel.onRefresh()
            }

        }

        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.checkIfAccessTokenChanged(authFlowValues.accessToken)

        }

    }


//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    override fun onDefaultClick(defaultName: String) {
//        val action = SubscribedFragmentDirections.actionSubscribedFragmentToRedditpageNavigation(
//            defaultName, "r", true
//        )
//
//        findNavController().navigate(action)
//    }
//
//    override fun onSubredditClick(subreddit: SubredditChildrenData) {
//        //TODO PUT LOGIC INTO VIEWMODEL NOT FRAGMENT
//        if(subreddit?.display_name_prefixed != null) {
//            val action = when (subreddit.subreddit_type) {
//                "user" -> {
//                    SubscribedFragmentDirections.actionSubscribedFragmentToRedditpageNavigation(
//                        subreddit.display_name_prefixed.substring(2), "user" , false
//                    )
//                }
//                else -> {
//                    SubscribedFragmentDirections.actionSubscribedFragmentToRedditpageNavigation(
//                        subreddit.display_name_prefixed.substring(2), "r", false)
//                }
//            }
//            findNavController().navigate(action)
//        }
//
//    }

}


