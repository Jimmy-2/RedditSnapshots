package com.jimmywu.snapshotsforreddit.ui.common.moreoptions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jimmywu.snapshotsforreddit.databinding.FragmentMoreOptionsDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoreOptionsDialogFragment : BottomSheetDialogFragment()  {

    private val viewModel: MoreOptionsViewModel by viewModels()
    private val navigationArgs: MoreOptionsDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { (view?.parent as ViewGroup).background = ColorDrawable(Color.TRANSPARENT) }

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val binding = FragmentMoreOptionsDialogBinding.bind(view)


        val postName = navigationArgs.postKeyName
        val postRedditPageAndSort = navigationArgs.postKeyPageAndSort
        val postSaveStatus = navigationArgs.postSaveStatus
        val postVoteStatus = navigationArgs.postVoteStatus
        val postAuthor = navigationArgs.postAuthor
        val postSubreddit = navigationArgs.postSubreddit


        binding.apply {
            textviewPostAuthor.text = postAuthor
            textviewPostSubreddit.text = postSubreddit

            if(postSaveStatus) {
                textviewSaveButton.text = "Unsave"
            }else {
                textviewSaveButton.text = "Save"
            }




            cardCancelButton.setOnClickListener {
                dismiss()
            }


            buttonSave.setOnClickListener{
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.onSaveClicked(postName,postRedditPageAndSort)

                    //dismiss dialog after completing an action such as saving/unsaving, upvoting/downvoting
                    viewModel.actionCompleted.collect {
                        if(it) {
                            dismiss()
                        }
                    }
                }



            }

            buttonUpvote.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.onUpvoteClicked(postName,postRedditPageAndSort,postVoteStatus)

                    //dismiss dialog after completing an action such as saving/unsaving, upvoting/downvoting
                    viewModel.actionCompleted.collect {
                        if(it) {
                            dismiss()
                        }
                    }
                }
            }

            buttonDownvote.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.onDownvoteClicked(postName,postRedditPageAndSort,postVoteStatus)

                    //dismiss dialog after completing an action such as saving/unsaving, upvoting/downvoting
                    viewModel.actionCompleted.collect {
                        if(it) {
                            dismiss()
                        }
                    }
                }
            }


        }

//        // User clicked the Cancel button; just exit the dialog without saving the data

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentMoreOptionsDialogBinding.inflate(inflater, container, false).root
    }
}