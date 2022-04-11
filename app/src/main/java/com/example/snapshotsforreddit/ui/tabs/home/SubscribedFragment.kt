package com.example.snapshotsforreddit.ui.tabs.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentSubscribedBinding
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject
import dagger.hilt.android.AndroidEntryPoint


//TODO: TURN THIS TO SIDE SHEET ON LEFT SIDE

@AndroidEntryPoint
class SubscribedFragment : Fragment(R.layout.fragment_subscribed), SubscribedAdapter.OnItemClickListener{
    private val viewModel: SubscribedViewModel by viewModels()

    private var _binding: FragmentSubscribedBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("HELLO subscribed created")

        _binding  = FragmentSubscribedBinding.bind(view)

        val subscribedAdapter = SubscribedAdapter(this)

        binding.apply {
            recyclerviewSubreddits.setHasFixedSize(true)
            recyclerviewSubreddits.adapter = subscribedAdapter
        }


        //whenever authFlow is changed (getting new accesstoken/refreshtoken), we will refresh the subscribed subreddits list
        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.checkIfAccessTokenChanged(authFlowValues.accessToken)
        }/*
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.changeAT(viewModel.authFlow.first().accessToken)
        }
        */


        //whenever the subscribed subreddits list is changed, we will refresh the recyclerview
        viewModel.subreddits.observe(viewLifecycleOwner) {
            //connect data to adapter
            subscribedAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(subreddit: SubscribedChildrenObject) {
        if(subreddit.data?.display_name_prefixed != null) {
            val action = if(subreddit.data.subreddit_type == "user") {
                 SubscribedFragmentDirections.actionSubscribedFragmentToSubredditFragment(
                    subreddit.data.display_name_prefixed.substring(2), "user"
                )
            }else {
                SubscribedFragmentDirections.actionSubscribedFragmentToSubredditFragment(
                    subreddit.data.display_name_prefixed.substring(2), "r")
            }
            findNavController().navigate(action)
        }

    }

}


/*
@AndroidEntryPoint
class SubscribedFragment : Fragment(R.layout.fragment_subscribed_subreddits), SubscribedAdapter.OnItemClickListener {
    private val viewModel: SubscribedViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //already inflated in constructor. We could inflate it in oncreateview this way:
        /*
        _binding = FragmentDownloadedPostsBinding.inflate(inflater, container, false)
        return binding.root
         */
        val binding = FragmentSubscribedSubredditsBinding.bind(view)

        val subscribedSubredditsAdapter = SubscribedAdapter(this)

        binding.apply {
            recyclerViewSubreddits.apply {
                adapter = subscribedSubredditsAdapter
                layoutManager = LinearLayoutManager(this.context)
                setHasFixedSize(true) // optimization for recyclerview if it doesnt change size in screen
            }//basically same as
            // binding.downloadedList.adapter = downloadedPostsTestAdapter
            // binding.downloadedList.layoutManager = LinearLayoutManager(this.context)


            buttonAuth.setOnClickListener {
                val intent = Uri.parse(viewModel.authSignInURL.value)
                val actionView = Intent(Intent.ACTION_VIEW, intent)
                startActivity(actionView)
            }


        }

        viewModel.subreddits.observe(viewLifecycleOwner)  {

        }

        //whenever authFlow is changed (getting new accesstoken/refreshtoken), we will refresh the subscribed subreddits list
        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.getSubscribedSubreddits(authFlowValues.accessToken)
        }

        //whenever the subscribed subreddits list is changed, we will refresh the recyclerview
        viewModel.subItemsTest.observe(viewLifecycleOwner) {
            subscribedSubredditsAdapter.submitList(it)
            //diffutil will do the other logic such as calcualting the differences, etc. If no differences, list will appear the same
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscribed_subreddits, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity().intent != null && requireActivity().intent.action == Intent.ACTION_VIEW) {
            val uri: Uri? = requireActivity().intent.data
            viewModel.checkCode(uri)

        }
    }

    override fun onItemClick(subreddit: SubscribedChildrenObject) {
        println("HELLO")
    }

}
 */