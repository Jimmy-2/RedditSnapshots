package com.jimmywu.snapshotsforreddit.ui.common.moreoptions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jimmywu.snapshotsforreddit.databinding.FragmentMoreOptionsDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreOptionsDialogFragment : BottomSheetDialogFragment()  {

    private val viewModel: MoreOptionsViewModel by viewModels()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener { (view?.parent as ViewGroup).background = ColorDrawable(Color.TRANSPARENT) }

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val binding = FragmentMoreOptionsDialogBinding.bind(view)




//        // User clicked the Cancel button; just exit the dialog without saving the data
        binding.cardCancelButton.setOnClickListener {
            dismiss()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentMoreOptionsDialogBinding.inflate(inflater, container, false).root
    }
}