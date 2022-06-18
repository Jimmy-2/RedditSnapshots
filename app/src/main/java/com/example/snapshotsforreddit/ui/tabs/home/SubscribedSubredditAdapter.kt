package com.example.snapshotsforreddit.ui.tabs.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.data.room.cache.SubscribedSubreddit
import com.example.snapshotsforreddit.databinding.ItemHeaderBinding
import com.example.snapshotsforreddit.databinding.ItemSubscribedBinding
import com.example.snapshotsforreddit.databinding.ItemSubscribedDefaultBinding

class SubscribedSubredditAdapter: ListAdapter<SubscribedSubreddit, RecyclerView.ViewHolder>(SubredditComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DEFAULT -> DefaultSubscribedSubredditViewHolder(
                ItemSubscribedDefaultBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            SUBREDDIT -> SubscribedSubredditViewHolder(
                ItemSubscribedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            HEADER -> HeaderViewHolder(
                ItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Error with view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                is DefaultSubscribedSubredditViewHolder -> holder.bind(currentItem)
                is SubscribedSubredditViewHolder -> holder.bind(currentItem)
                is HeaderViewHolder -> holder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)?.dataKind) {
            "default" -> DEFAULT
            "header" -> HEADER
            else -> SUBREDDIT
        }
    }

    inner class DefaultSubscribedSubredditViewHolder(val binding: ItemSubscribedDefaultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            binding.buttonSubredditHome.setOnClickListener {
                navigateToSubreddit("Home")
            }
            binding.buttonSubredditPopular.setOnClickListener {
                navigateToSubreddit("popular")
            }
            binding.buttonSubredditAll.setOnClickListener {
                navigateToSubreddit("all")
            }
        }

        private fun navigateToSubreddit(defaultName: String) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position)
                if (item != null) {
//                    onClickListener.onDefaultClick(defaultName)
                }
            }
        }


        fun bind(subreddit: SubscribedSubreddit) {

            binding.apply {

            }
        }
    }

    inner class HeaderViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subredditObject: SubscribedSubreddit) {
            binding.apply {
                textviewHeader.visibility = View.VISIBLE
                textviewHeader.text = "FOLLOWING"
            }
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

    companion object {
        private const val DEFAULT = 0
        private const val SUBREDDIT = 1
        private const val HEADER = 2
        private const val ERROR = 3
    }
}