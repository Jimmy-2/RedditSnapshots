package com.example.snapshotsforreddit.ui.post

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.BaseApplication
import com.example.snapshotsforreddit.data.UserPreferences
import com.example.snapshotsforreddit.database.Post
import com.example.snapshotsforreddit.databinding.FragmentPostDetailBinding


class PostDetailFragment: Fragment() {
    private val navigationArgs: PostDetailFragmentArgs by navArgs()
    private lateinit var userPreferences: UserPreferences

    private val viewModel: PostDetailViewModel by activityViewModels {
        PostDetailViewModelFactory(
            (activity?.application as BaseApplication).database
                .postDao(), userPreferences
        )
    }
    lateinit var post: Post

    private fun addNewItem() {
        viewModel.addNewPost()
    }


    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())

        viewModel.userPreferencesFlow.observe(viewLifecycleOwner, { value ->
            viewModel.getPostDetail(value, "bearer")

        })

        _binding = FragmentPostDetailBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
         binding.saveAction.setOnClickListener {
            println("HELLO")
            addNewItem()
        }
         */
        val subreddit = navigationArgs.postSubreddit
        val id = navigationArgs.postId
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel

        }
        viewModel.retrievePostLink(subreddit, id)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}