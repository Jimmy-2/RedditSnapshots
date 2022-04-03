package com.example.snapshotsforreddit.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.snapshotsforreddit.data.Repository.TokensDatastore
import com.example.snapshotsforreddit.databinding.FragmentAuthBinding
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
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainFragment", "onResume called")
        if (requireActivity().intent != null && requireActivity().intent.action == Intent.ACTION_VIEW) {
            val uri: Uri? = requireActivity().intent.data
            viewModel.checkCode(uri)

        }
    }

    fun goToFrontPage() {
        //findNavController().navigate(R.id.action_authFragment_to_frontPageFragment)
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