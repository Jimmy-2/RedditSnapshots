package com.example.snapshotsforreddit.ui.redditpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.adapter.FrontPageAdapter
import com.example.snapshotsforreddit.adapter.FrontPageListener
import com.example.snapshotsforreddit.data.TokensDatastore
import com.example.snapshotsforreddit.databinding.FragmentFrontPageBinding



class FrontPageFragment : Fragment() {
    private lateinit var tokensDatastore: TokensDatastore
    private val viewModel: FrontPageViewModel by activityViewModels(){
        FrontPageViewModelFactory(tokensDatastore)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Initialize tokensDatastore
        tokensDatastore = TokensDatastore(requireContext())

        viewModel.tokensDataStoreFlow.observe(viewLifecycleOwner, { value ->
            viewModel.getPosts(value, "bearer")

        })

        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }


        val binding = FragmentFrontPageBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.postsList.adapter = FrontPageAdapter(FrontPageListener {
            val action = FrontPageFragmentDirections.actionFrontPageFragmentToPostDetailFragment(
                //just pass the object over
               it!!
            )
            this.findNavController().navigate(action,options)
            //findNavController().navigate(R.id.action_frontPageFragment_to_postDetailFragment)

        })

        //viewModel.getPosts()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

    }
}