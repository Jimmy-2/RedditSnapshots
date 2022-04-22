package com.example.snapshotsforreddit.ui.tabs.account.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentAccountInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountInfoFragment : Fragment(R.layout.fragment_account_info) {
    val viewModel: AccountInfoViewModel by viewModels()

    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding  = FragmentAccountInfoBinding.bind(view)


        binding.apply {

        }


    }


}