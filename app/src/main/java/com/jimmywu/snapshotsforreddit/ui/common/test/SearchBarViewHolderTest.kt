package com.jimmywu.snapshotsforreddit.ui.common.test

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.jimmywu.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePost
import com.jimmywu.snapshotsforreddit.databinding.ItemSearchBarBinding

class SearchBarViewHolderTest(
    private val binding: ItemSearchBarBinding,
    private val onSearchSubmit: (Int, String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (query != null && query != "") {
                        onSearchSubmit(position, query)
                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    fun bind(post: RedditPagePost) {
        val currentSubreddit = post.redditPageName
        binding.apply {
            if (currentSubreddit == "" || currentSubreddit == "Home") {
                searchview.queryHint = "Search"
            } else {
                searchview.queryHint = "Search r/$currentSubreddit"
            }

        }
    }

}