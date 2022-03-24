package com.example.snapshotsforreddit.ui.redditpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.snapshotsforreddit.adapter.FrontPageAdapter
import com.example.snapshotsforreddit.adapter.FrontPageListener
import com.example.snapshotsforreddit.data.UserPreferences
import com.example.snapshotsforreddit.databinding.FragmentFrontPageBinding



class FrontPageFragment : Fragment() {
    private lateinit var userPreferences: UserPreferences
    private val viewModel: FrontPageViewModel by activityViewModels(){
        FrontPageViewModelFactory(userPreferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Initialize UserPreferences
        userPreferences = UserPreferences(requireContext())

        viewModel.userPreferencesFlow.observe(viewLifecycleOwner, { value ->
            viewModel.getPosts(value, "bearer")

        })



        val binding = FragmentFrontPageBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.postsList.adapter = FrontPageAdapter(FrontPageListener {
            val action = FrontPageFragmentDirections.actionFrontPageFragmentToPostDetailFragment(it.subreddit!!,it.id!!)
            this.findNavController().navigate(action)
            //findNavController().navigate(R.id.action_frontPageFragment_to_postDetailFragment)

        })

        //viewModel.getPosts()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}