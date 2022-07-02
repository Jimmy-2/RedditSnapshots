package com.jimmywu.snapshotsforreddit.ui.tabs.inbox

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jimmywu.snapshotsforreddit.R
import com.jimmywu.snapshotsforreddit.databinding.FragmentInboxItemsBinding
import com.jimmywu.snapshotsforreddit.ui.common.loadstate.RedditLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InboxItemsFragment: Fragment(R.layout.fragment_inbox_items) {
    private val navigationArgs: InboxItemsFragmentArgs by navArgs()
    private val viewModel: InboxViewModel by viewModels()

    private var _binding: FragmentInboxItemsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding  = FragmentInboxItemsBinding.bind(view)

        val inboxType = navigationArgs.inboxType
        viewModel.loadInbox(inboxType)

        val inboxAdapter = InboxAdapter()

        binding.apply {
            recyclerviewInboxItems.setHasFixedSize(true)
            recyclerviewInboxItems.adapter = inboxAdapter.withLoadStateHeaderAndFooter(
                header = RedditLoadStateAdapter { inboxAdapter.retry() },
                footer = RedditLoadStateAdapter { inboxAdapter.retry() }
            )
            refreshInboxItems.setOnRefreshListener { inboxAdapter.refresh() }
        }

        viewModel.inboxItems.observe(viewLifecycleOwner) {
            //connect data to adapter
            inboxAdapter.submitData(viewLifecycleOwner.lifecycle, it)

        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}