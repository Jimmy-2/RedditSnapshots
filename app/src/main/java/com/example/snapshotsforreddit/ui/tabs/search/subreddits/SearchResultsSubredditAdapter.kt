package com.example.snapshotsforreddit.ui.tabs.search.subreddits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.ItemSearchResultsSubredditBinding
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData
import com.example.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.example.snapshotsforreddit.util.getShortenedValue

class SearchResultsSubredditAdapter(private val onClickListener: OnItemClickListener) : PagingDataAdapter<SubredditChildrenData, SearchResultsSubredditAdapter.SubredditViewHolder>(SUBREDDIT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditViewHolder {
        val binding = ItemSearchResultsSubredditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubredditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubredditViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class SubredditViewHolder(private val binding: ItemSearchResultsSubredditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onClickListener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(searchResultsSubreddit : SubredditChildrenData) {
            binding.apply {
                if(searchResultsSubreddit != null ) {
                    textviewSubredditName.text = searchResultsSubreddit.display_name_prefixed
                    textviewSubredditSummary.text = searchResultsSubreddit.public_description
                    textviewSubredditStats.text = "${getShortenedValue(searchResultsSubreddit.subscribers)} Subscribers * ${calculateAgeDifferenceLocalDateTime(searchResultsSubreddit.created_utc?: 0 ,0)} Old"
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(subreddit: SubredditChildrenData)
    }


    companion object {
        private val SUBREDDIT_COMPARATOR = object : DiffUtil.ItemCallback<SubredditChildrenData>() {
            override fun areItemsTheSame(oldItem: SubredditChildrenData, newItem: SubredditChildrenData) =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: SubredditChildrenData, newItem: SubredditChildrenData) =
                oldItem == newItem
        }
    }
}