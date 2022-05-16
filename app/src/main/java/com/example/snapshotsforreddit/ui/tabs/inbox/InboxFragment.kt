package com.example.snapshotsforreddit.ui.tabs.inbox
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentInboxBinding


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InboxFragment : Fragment(R.layout.fragment_inbox){

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentInboxBinding.bind(view)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}