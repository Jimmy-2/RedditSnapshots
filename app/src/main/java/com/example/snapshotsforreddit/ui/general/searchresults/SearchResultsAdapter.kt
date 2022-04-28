package com.example.snapshotsforreddit.ui.general.searchresults

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.ItemPostBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.ui.general.PostViewHolder

class SearchResultsAdapter(private val onClickListener: OnItemClickListener) : PagingDataAdapter<RedditChildrenObject, RecyclerView.ViewHolder>(SEARCH_RESULT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(this@SearchResultsAdapter, onClickListener, binding)

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
        override fun onItemClick(post: RedditChildrenObject)
        override fun onVoteClick(post: RedditChildrenObject, type: Int)
        //fun onVoteClick(post: RedditChildrenObject, type: Int, position: Int)
    }

    companion object {
        private val SEARCH_RESULT_COMPARATOR = object : DiffUtil.ItemCallback<RedditChildrenObject>() {
            override fun areItemsTheSame(oldItem: RedditChildrenObject, newItem: RedditChildrenObject): Boolean {
                //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                //TODO FIX ISSUE WITH IMAGE DUPLICATION
                return oldItem.data?.name == newItem.data?.name
            }
            override fun areContentsTheSame(oldItem: RedditChildrenObject, newItem: RedditChildrenObject): Boolean {
                return oldItem == newItem
            }
        }
    }


}