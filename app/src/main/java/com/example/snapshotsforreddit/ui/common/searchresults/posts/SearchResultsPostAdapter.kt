package com.example.snapshotsforreddit.ui.common.searchresults.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.ItemPostBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.ui.common.viewholders.PostViewHolder

class SearchResultsPostAdapter(private val onClickListener: OnItemClickListener) : PagingDataAdapter<RedditChildrenData, RecyclerView.ViewHolder>(
    SEARCH_RESULT_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(this@SearchResultsPostAdapter, onClickListener, binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                is PostViewHolder -> holder.bind(currentItem)
            }
        }
    }

    interface OnItemClickListener : PostViewHolder.OnItemClickListener {
        override fun onItemClick(post: RedditChildrenData)
        override fun onVoteClick(post: RedditChildrenData, type: Int)
        //fun onVoteClick(post: RedditChildrenData, type: Int, position: Int)
    }

    companion object {
        private val SEARCH_RESULT_COMPARATOR = object : DiffUtil.ItemCallback<RedditChildrenData>() {
            override fun areItemsTheSame(oldItem: RedditChildrenData, newItem: RedditChildrenData): Boolean {
                //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                //TODO FIX ISSUE WITH IMAGE DUPLICATION
                return oldItem.name == newItem.name
            }
            override fun areContentsTheSame(oldItem: RedditChildrenData, newItem: RedditChildrenData): Boolean {
                return oldItem == newItem
            }
        }
    }


}