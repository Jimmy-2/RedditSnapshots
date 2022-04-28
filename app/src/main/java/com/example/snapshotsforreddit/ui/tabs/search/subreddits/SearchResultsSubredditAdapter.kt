package com.example.snapshotsforreddit.ui.tabs.search.subreddits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.ItemSearchResultsSubredditBinding
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenObject
import com.example.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.example.snapshotsforreddit.util.getShortenedValue

class SearchResultsSubredditAdapter(private val onClickListener: OnItemClickListener) : PagingDataAdapter<SubredditChildrenObject, SearchResultsSubredditAdapter.SubredditViewHolder>(SUBREDDIT_COMPARATOR) {

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

        fun bind(searchResultsSubreddit : SubredditChildrenObject) {
            binding.apply {
                if(searchResultsSubreddit.data != null ) {
                    textviewSubredditName.text = searchResultsSubreddit.data.display_name_prefixed
                    textviewSubredditSummary.text = searchResultsSubreddit.data.public_description
                    textviewSubredditStats.text = "${getShortenedValue(searchResultsSubreddit.data.subscribers)} Subscribers * ${calculateAgeDifferenceLocalDateTime(searchResultsSubreddit.data.created_utc?: 0 ,0)} Old"
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(subreddit: SubredditChildrenObject)
    }


    companion object {
        private val SUBREDDIT_COMPARATOR = object : DiffUtil.ItemCallback<SubredditChildrenObject>() {
            override fun areItemsTheSame(oldItem: SubredditChildrenObject, newItem: SubredditChildrenObject) =
                oldItem.data?.display_name_prefixed == newItem.data?.display_name_prefixed

            override fun areContentsTheSame(oldItem: SubredditChildrenObject, newItem: SubredditChildrenObject) =
                oldItem == newItem
        }
    }
}