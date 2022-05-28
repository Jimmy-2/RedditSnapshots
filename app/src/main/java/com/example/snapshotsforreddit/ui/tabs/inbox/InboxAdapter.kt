package com.example.snapshotsforreddit.ui.tabs.inbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.ItemInboxBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenData


class InboxAdapter () :
    PagingDataAdapter<RedditChildrenData, InboxAdapter.InboxItemViewHolder>(INBOX_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxItemViewHolder {
        val binding = ItemInboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InboxItemViewHolder(binding)
    }



    override fun onBindViewHolder(holder: InboxItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }



    inner class InboxItemViewHolder(private val binding: ItemInboxBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {

            }
        }

        fun bind(inboxItem : RedditChildrenData) {
            binding.apply {
                if(inboxItem != null) {
                    textviewInboxTitle.text = inboxItem.link_title
                    textviewInboxBody.text = inboxItem.body
                    //TODO textviewInboxPostRepliesBody.text = inboxItem.body_html
                    textviewInboxSubreddit.text = inboxItem.subreddit
                    textviewInboxAuthor.text = inboxItem.author
                    textviewInboxAge.text = inboxItem.created_utc.toString()
                }
            }
        }
    }

    companion object {

        private val INBOX_COMPARATOR =
            object : DiffUtil.ItemCallback<RedditChildrenData>() {
                override fun areItemsTheSame(
                    oldItem: RedditChildrenData,
                    newItem: RedditChildrenData
                ): Boolean {

                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: RedditChildrenData,
                    newItem: RedditChildrenData
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }

}