package com.example.snapshotsforreddit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.SubscribedSubredditsItemBinding
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject

class SubTestAdapter :
    PagingDataAdapter<SubscribedChildrenObject, SubTestAdapter.SubscribedSubredditsViewHolder>(
        SUBREDDIT_COMPARATOR
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscribedSubredditsViewHolder {
        val binding = SubscribedSubredditsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SubscribedSubredditsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscribedSubredditsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    class SubscribedSubredditsViewHolder(private val binding: SubscribedSubredditsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subredditObject: SubscribedChildrenObject) {
            binding.apply {
                titleSubredditItem.text = subredditObject.data!!.display_name_prefixed
                favoriteSubredditItem.isVisible = subredditObject.data.user_has_favorited!!
                println(subredditObject.data.user_has_favorited!!)
            }
        }
    }


    companion object {
        private val SUBREDDIT_COMPARATOR =
            object : DiffUtil.ItemCallback<SubscribedChildrenObject>() {
                override fun areItemsTheSame(
                    oldItem: SubscribedChildrenObject,
                    newItem: SubscribedChildrenObject
                ): Boolean {

                    //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                    return oldItem.data == newItem.data
                }

                override fun areContentsTheSame(
                    oldItem: SubscribedChildrenObject,
                    newItem: SubscribedChildrenObject
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }




}