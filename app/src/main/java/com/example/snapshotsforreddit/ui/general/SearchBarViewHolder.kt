package com.example.snapshotsforreddit.ui.general

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.ItemSearchBarBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject

class SearchBarViewHolder(
    private val onClickListener: OnItemClickListener,
    private val binding: ItemSearchBarBinding) : RecyclerView.ViewHolder(binding.root) {

    private var post: RedditChildrenObject? = null
    private var currentSubreddit: String? = null

    init {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (currentSubreddit!= null) {
                    onClickListener.onSearchSubmit(query, currentSubreddit!!)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    fun bind(postObject: RedditChildrenObject) {
        this.post = postObject
        this.currentSubreddit = post?.defaults?.type
        binding.apply {
            searchview.queryHint = "Search r/$currentSubreddit"
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: RedditChildrenObject)
        fun onSearchSubmit(query: String?, subredditName: String)
    }
}