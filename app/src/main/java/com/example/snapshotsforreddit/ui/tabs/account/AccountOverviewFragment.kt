package com.example.snapshotsforreddit.ui.tabs.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentAccountOverviewBinding
import com.example.snapshotsforreddit.ui.general.redditpage.RedditPageAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountOverviewFragment: Fragment(R.layout.fragment_account_overview) {
    private val viewModel: AccountOverviewViewModel by viewModels()

    private var _binding: FragmentAccountOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentAccountOverviewBinding.bind(view)

        val accountOverviewAdapter = AccountOverviewAdapter ()

        binding.apply {
            recyclerviewAccount.setHasFixedSize(true)
            recyclerviewAccount.adapter = accountOverviewAdapter
            buttonDialog.setOnClickListener { loginView ->
                loginView.findNavController().navigate(
                    AccountOverviewFragmentDirections.actionAccountOverviewFragmentToLoginDialogFragment()
                )
            }
        }

        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.checkIfUsernameChanged(authFlowValues.username)
        }

        viewModel.accountOverviewItems.observe(viewLifecycleOwner) {
            //connect data to adapter
            accountOverviewAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }


    }

    override fun onResume() {
        super.onResume()
        //on successful authentication
        if (requireActivity().intent != null && requireActivity().intent.action == Intent.ACTION_VIEW) {
            val uri: Uri? = requireActivity().intent.data
            viewModel.checkCode(uri)
        }
        //clear intent after successful retrieval of uri
        requireActivity().intent = null

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

