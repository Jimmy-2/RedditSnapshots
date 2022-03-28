package com.example.snapshotsforreddit.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentSubscribedSubredditsBinding
import dagger.hilt.android.AndroidEntryPoint


/*
@AndroidEntryPoint
class SubTestFragment: Fragment(R.layout.fragment_subscribed_subreddits) {
    private val viewModel: SubscribedSubredditsViewModel by viewModels()

    private var _binding: FragmentSubscribedSubredditsBinding? = null
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSubscribedSubredditsBinding.bind(view)

        val adapter = SubTestAdapter()

        binding.apply {
            recyclerViewSubreddits.setHasFixedSize(true)
            recyclerViewSubreddits.adapter = adapter

            buttonAuth.setOnClickListener {
                val intent = Uri.parse(viewModel.authSignInURL.value)
                val actionView = Intent(Intent.ACTION_VIEW, intent)
                startActivity(actionView)
            }

        }


        viewModel.subreddits.observe(viewLifecycleOwner)  {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        //whenever authFlow is changed (getting new accesstoken/refreshtoken), we will refresh the subscribed subreddits list
        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.getSubscribedSubreddits(authFlowValues.accessToken)
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
 */