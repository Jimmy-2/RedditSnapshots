package com.example.snapshotsforreddit.ui.general.login


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.snapshotsforreddit.databinding.FragmentLoginDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginDialogBinding.bind(view)


        binding.apply {
            buttonLogin2.setOnClickListener {
                //browser authentication
                val intent = Uri.parse(viewModel.authSignInURL.value)
                val actionView = Intent(Intent.ACTION_VIEW, intent)
                startActivity(actionView)
            }

            buttonLogout2.setOnClickListener {
                viewModel.onLogoutClicked()
            }

        }
        //TODO: Use onlyt 1 button and have a ENUM class to change text and function of the button instead of using 2
        //observe the login state of the user so we can hide the sign in or the logout buttons depending on the login state
        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            viewModel.accessToken.value = authFlowValues.accessToken
            viewModel.refreshToken.value = authFlowValues.refreshToken

            if(authFlowValues.loginState) {
                binding.buttonLogin2.visibility = View.GONE
                binding.buttonLogout2.visibility = View.VISIBLE
            }else {
                binding.buttonLogin2.visibility = View.VISIBLE
                binding.buttonLogout2.visibility = View.GONE
            }

        }


    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentLoginDialogBinding.inflate(inflater, container, false).root
    }
}

/*
//TODO: DEEP LINK INTO FRONT PAGE. COPY REDDIT APP INSTEAD
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)


        binding.apply {
            buttonLogin.setOnClickListener {
                //browser authentication
                val intent = Uri.parse(viewModel.authSignInURL.value)
                val actionView = Intent(Intent.ACTION_VIEW, intent)
                startActivity(actionView)
            }

            buttonLogout.setOnClickListener {
                viewModel.onLogoutClicked()
            }

        }
        //observe the login state of the user so we can hide the sign in or the logout buttons depending on the login state
        viewModel.authFlow.observe(viewLifecycleOwner) { authFlowValues ->
            if(authFlowValues.loginState) {
                binding.buttonLogin.visibility = View.GONE
                binding.buttonLogout.visibility = View.VISIBLE
            }else {
                binding.buttonLogin.visibility = View.VISIBLE
                binding.buttonLogout.visibility = View.GONE
            }

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

 */