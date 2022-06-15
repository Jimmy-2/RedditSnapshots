package com.example.snapshotsforreddit.ui.tabs.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.snapshotsforreddit.data.room.cache.SubscribedSubreddit
import com.example.snapshotsforreddit.databinding.ItemSubscribedBinding

class SubscribedSubredditAdapter: ListAdapter<SubscribedSubreddit, SubscribedSubredditViewHolder>(SubredditComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribedSubredditViewHolder {
        val binding = ItemSubscribedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SubscribedSubredditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscribedSubredditViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }



    class SubredditComparator : DiffUtil.ItemCallback<SubscribedSubreddit>() {
        override fun areItemsTheSame(oldItem: SubscribedSubreddit, newItem: SubscribedSubreddit): Boolean {
            //if contents are the same but are at different positions, this method will know how to move that item
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: SubscribedSubreddit, newItem: SubscribedSubreddit): Boolean {
            return oldItem == newItem
        }

    }


}