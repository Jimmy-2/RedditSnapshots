package com.example.snapshotsforreddit.ui.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.snapshotsforreddit.BaseApplication
import com.example.snapshotsforreddit.data.TokensDatastore
import com.example.snapshotsforreddit.data.room.Post
import com.example.snapshotsforreddit.databinding.FragmentPostDetailBinding


class PostDetailFragment: Fragment() {
    private val navigationArgs: PostDetailFragmentArgs by navArgs()
    private lateinit var tokensDatastore: TokensDatastore

    private val viewModel: PostDetailViewModel by activityViewModels {
        PostDetailViewModelFactory(
            (activity?.application as BaseApplication).database
                .postDao(), tokensDatastore
        )
    }
    lateinit var post: Post


    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tokensDatastore = TokensDatastore(requireContext())

        _binding = FragmentPostDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
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
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = viewModel

        }
         */


        viewModel.tokensDatastoreFlow.observe(viewLifecycleOwner, { accessToken ->
            viewModel.getPostDetail(accessToken, "bearer")
            println("HELLO REFRESH")
        })


        val postData = navigationArgs.postData
        viewModel.retrievePostData(postData)


    }


    //do not use this. test out screen rotation. everytime screen rotates, everything runs
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetView()

    }


}