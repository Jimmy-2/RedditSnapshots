package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.inbox


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jimmywu.snapshotsforreddit.R
import com.jimmywu.snapshotsforreddit.databinding.FragmentInboxBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InboxFragment : Fragment(R.layout.fragment_inbox) {
    private val viewModel: InboxViewModel by viewModels()

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding  = FragmentInboxBinding.bind(view)


        //TODO CHANGE THE ROUNDED SHAPES IN INBOX, SEARCH AND ACCOUNT TO 1 BIG ROUNDED VIEW WITH TEXTVIEWS INSIDE
        binding.apply {

            buttonInbox.setOnClickListener{
                findNavController().navigate(InboxFragmentDirections.actionInboxFragmentToInboxItemsFragment("inbox","Inbox"))
            }

            buttonUnread.setOnClickListener{
                findNavController().navigate(InboxFragmentDirections.actionInboxFragmentToInboxItemsFragment("unread","Unread"))
            }

            buttonPostReplies.setOnClickListener{
                findNavController().navigate(InboxFragmentDirections.actionInboxFragmentToInboxItemsFragment("selfreply", "Post Replies"))
            }

            buttonCommentReplies.setOnClickListener{
                findNavController().navigate(InboxFragmentDirections.actionInboxFragmentToInboxItemsFragment("comments","Comment Replies"))
            }

            buttonMentions.setOnClickListener{
                findNavController().navigate(InboxFragmentDirections.actionInboxFragmentToInboxItemsFragment("mentions", "Mentions"))
            }

            buttonMessages.setOnClickListener{
                findNavController().navigate(InboxFragmentDirections.actionInboxFragmentToInboxItemsFragment("messages","Messages"))
            }

        }

    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}