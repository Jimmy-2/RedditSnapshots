package com.example.snapshotsforreddit.ui.auth

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.FragmentOAuth2Binding
import com.example.snapshotsforreddit.model.RedditViewModel


class OAuth2Fragment : Fragment() {

    private val viewModel: RedditViewModel by activityViewModels()

    private var _binding: FragmentOAuth2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOAuth2Binding.inflate(inflater)
        binding.viewModel = viewModel

        binding.button.setOnClickListener {
            val intent = Uri.parse(viewModel.authSignInURL.value)
            val actionView = Intent(Intent.ACTION_VIEW, intent)
            startActivity(actionView)
        }
        Log.i("MainFragment", "onCreateView called")

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainFragment", "onResume called")
        if (requireActivity().intent != null && requireActivity().intent.action == Intent.ACTION_VIEW) {
            val uri = requireActivity().intent.data
            if (uri!!.getQueryParameter("error") != null) {
                val error = uri.getQueryParameter("error")
                Log.e(ContentValues.TAG, "An error has occurred : $error")
            } else {
                val state = uri.getQueryParameter("state")
                if (state == viewModel.state) {
                    viewModel.setCode(uri.getQueryParameter("code"))
                    //goToFrontPage()
                }
            }
        }
    }

    fun goToFrontPage() {
        findNavController().navigate(R.id.action_OAuth2Fragment_to_frontPageFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel
            authFragment = this@OAuth2Fragment
        }
    }

}