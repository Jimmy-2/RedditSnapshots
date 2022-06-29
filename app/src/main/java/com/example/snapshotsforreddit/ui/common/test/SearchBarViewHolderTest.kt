package com.example.snapshotsforreddit.ui.common.test

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePost
import com.example.snapshotsforreddit.databinding.ItemSearchBarBinding

class SearchBarViewHolderTest  (
    private val binding: ItemSearchBarBinding,
//    private val onSearchSubmit: (String?, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

    private var post: RedditPagePost? = null
    private var currentSubreddit: String? = null

    init {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (currentSubreddit != null) {
//                    onSearchSubmit(query, currentSubreddit!!)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    fun bind(post: RedditPagePost) {
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

}