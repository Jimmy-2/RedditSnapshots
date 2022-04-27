package com.example.snapshotsforreddit.ui.general.redditpage


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.PostItemBinding
import com.example.snapshotsforreddit.databinding.SearchBarItemBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject


//TODO for now use notifyItemChanged(position) until db pagination is implemented
class RedditPageAdapter(private val onClickListener: OnItemClickListener) : PagingDataAdapter<RedditChildrenObject, RecyclerView.ViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SEARCH -> SearchBarViewHolder(SearchBarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            //POST -> PostViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            POST -> PostViewHolder(this@RedditPageAdapter, onClickListener,PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Error with view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                //is SearchBarViewHolder -> holder.bind(currentItem)
                is PostViewHolder -> holder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.kind) {
            "search" -> SEARCH
            else -> POST


        }
    }

    inner class SearchBarViewHolder(private val binding: SearchBarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                val subredditName = getItem(0)?.data?.subreddit
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (subredditName != null) {
                        onClickListener.onSearchSubmit(query, subredditName)
                    }
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
    }


    interface OnItemClickListener : PostViewHolder.OnItemClickListener {
        fun onSearchSubmit(query: String?, subredditName: String)
        override fun onItemClick(post: RedditChildrenObject)
        override fun onVoteClick(post: RedditChildrenObject, type: Int)

    }

    companion object {
        private const val SEARCH = 0
        private const val POST = 1
        private const val ERROR = 2

        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<RedditChildrenObject>() {
            override fun areItemsTheSame(oldItem: RedditChildrenObject, newItem: RedditChildrenObject): Boolean {
                return oldItem.data?.name == newItem.data?.name
            }
            override fun areContentsTheSame(oldItem: RedditChildrenObject, newItem: RedditChildrenObject): Boolean {
                return oldItem == newItem
            }
        }
    }


}