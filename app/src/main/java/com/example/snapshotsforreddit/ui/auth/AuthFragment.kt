package com.example.snapshotsforreddit.ui.auth

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.data.TokensDatastore
import com.example.snapshotsforreddit.databinding.FragmentAuthBinding
import com.example.snapshotsforreddit.model.AuthViewModel
import com.example.snapshotsforreddit.model.AuthViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private lateinit var tokensDatastore: TokensDatastore

    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(tokensDatastore)
    }

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Initialize tokensDatastore
        tokensDatastore = TokensDatastore(requireContext())

        _binding = FragmentAuthBinding.inflate(inflater)
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
                    /*
                    lifecycleScope.launch {
                        tokensDatastore.saveAccessTokenToDataStore("HELLO")
                    }
                     */
                    viewModel.setCode(uri.getQueryParameter("code"))
                    //goToFrontPage()
                }
            }
        }
    }

    fun goToFrontPage() {

        findNavController().navigate(R.id.action_authFragment_to_frontPageFragment)
    }

    fun goToSaved() {
        //findNavController().navigate(R.id.action_authFragment_to_downloadedPostsTestFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            //viewModel = viewModel
            authFragment = this@AuthFragment
        }
        viewModel.tokensDatastoreFlow.observe(viewLifecycleOwner, { value ->
            println("HELLOssss $value")

        })

    }

}