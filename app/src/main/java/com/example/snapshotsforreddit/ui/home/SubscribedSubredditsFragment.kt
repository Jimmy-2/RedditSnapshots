package com.example.snapshotsforreddit.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentDownloadedPostsTestBinding
import com.example.snapshotsforreddit.databinding.FragmentSubscribedSubredditsBinding
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenData
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject
import com.example.snapshotsforreddit.ui.downloadedposts.DownloadedPostsTestAdapter
import com.example.snapshotsforreddit.ui.downloadedposts.DownloadedPostsTestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscribedSubredditsFragment : Fragment(R.layout.fragment_subscribed_subreddits){
    private val viewModel: SubscribedSubredditsViewModel by viewModels()
    private var _binding: FragmentSubscribedSubredditsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //already inflated in constructor. We could inflate it in oncreateview this way:
        /*
        _binding = FragmentDownloadedPostsBinding.inflate(inflater, container, false)
        return binding.root
         */
        _binding  = FragmentSubscribedSubredditsBinding.bind(view)

        val subscribedSubredditsAdapter = SubTestAdapter()

        binding.apply {
            recyclerViewSubreddits.setHasFixedSize(true)
            recyclerViewSubreddits.adapter = subscribedSubredditsAdapter





            buttonAuth.setOnClickListener {
                val intent = Uri.parse(viewModel.authSignInURL.value)
                val actionView = Intent(Intent.ACTION_VIEW, intent)
                startActivity(actionView)
            }


        }


/*

 */
        //whenever authFlow is changed (getting new accesstoken/refreshtoken), we will refresh the subscribed subreddits list
        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.changeAT(authFlowValues.accessToken)
        }
        /*
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.changeAT(viewModel.authFlow.first().accessToken)
        }
        */

        viewModel.subreddits.observe(viewLifecycleOwner) {
            subscribedSubredditsAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

    }


    override fun onResume() {
        super.onResume()
        if (requireActivity().intent != null && requireActivity().intent.action == Intent.ACTION_VIEW) {
            val uri: Uri? = requireActivity().intent.data
            viewModel.checkCode(uri)

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}


/*
@AndroidEntryPoint
class SubscribedSubredditsFragment : Fragment(R.layout.fragment_subscribed_subreddits), SubscribedSubredditsAdapter.OnItemClickListener {
    private val viewModel: SubscribedSubredditsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //already inflated in constructor. We could inflate it in oncreateview this way:
        /*
        _binding = FragmentDownloadedPostsBinding.inflate(inflater, container, false)
        return binding.root
         */
        val binding = FragmentSubscribedSubredditsBinding.bind(view)

        val subscribedSubredditsAdapter = SubscribedSubredditsAdapter(this)

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