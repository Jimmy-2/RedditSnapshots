package com.jimmywu.snapshotsforreddit.ui.common.viewholders

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.jimmywu.snapshotsforreddit.databinding.ItemSearchBarBinding
import com.jimmywu.snapshotsforreddit.network.responses.RedditChildrenData

class SearchBarViewHolder  (
    private val onClickListener: OnItemClickListener,
    private val binding: ItemSearchBarBinding) : RecyclerView.ViewHolder(binding.root) {

    private var post: RedditChildrenData? = null
    private var currentSubreddit: String? = null

    init {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (currentSubreddit != null) {
                    onClickListener.onSearchSubmit(query, currentSubreddit!!)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    fun bind(post: RedditChildrenData) {
        this.post = post
        this.currentSubreddit = post.subreddit
        binding.apply {
            if(currentSubreddit == null || currentSubreddit == "" || currentSubreddit == "Home") {
                searchview.queryHint = "Search"
            }else {
                searchview.queryHint = "Search r/$currentSubreddit"
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: RedditChildrenData)
        fun onSearchSubmit(query: String?, subredditName: String)

    }
}