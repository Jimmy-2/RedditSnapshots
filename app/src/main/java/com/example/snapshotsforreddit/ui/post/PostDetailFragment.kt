package com.example.snapshotsforreddit.ui.post

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import com.example.snapshotsforreddit.SavedPostsApplication
import com.example.snapshotsforreddit.database.Post
import com.example.snapshotsforreddit.databinding.FragmentPostDetailBinding


class PostDetailFragment: Fragment() {


    private val localViewModel: PostDetailViewModel by activityViewModels {
        PostDetailViewModelFactory(
            (activity?.application as SavedPostsApplication).database
                .postDao()
        )
    }
    lateinit var post: Post

    private fun addNewItem() {
        localViewModel.addNewPost()
    }


    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = localViewModel

        }

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