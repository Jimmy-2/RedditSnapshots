package com.jimmywu.snapshotsforreddit.ui.common.redditpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jimmywu.snapshotsforreddit.databinding.*
import com.jimmywu.snapshotsforreddit.network.responses.RedditChildrenData
import com.jimmywu.snapshotsforreddit.ui.common.viewholders.*

class RedditPageAdapter(
    private val onClickListener: OnItemClickListener,
) : PagingDataAdapter<RedditChildrenData, RecyclerView.ViewHolder>(POST_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //private var isCompact = false
//
//        isCompact =  runBlocking { = preferencesFlow.first().isCompactView}
//               viewLifecycleOwner.lifecycleScope.launch {
//                   isCompact =  preferencesFlow.first().isCompactView
//               }

        return when (viewType) {
            SEARCH -> SearchBarViewHolder(
                onClickListener,
                ItemSearchBarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )


            POST -> PostViewHolder(
                this@RedditPageAdapter, onClickListener,
                ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            POST_COMPACT -> PostCompactViewHolder(
                this@RedditPageAdapter, onClickListener,
                ItemPostCompactBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            SUBREDDIT_POST -> SubredditPostViewHolder(
                this@RedditPageAdapter, onClickListener,
                ItemSubredditPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            SUBREDDIT_POST_COMPACT -> SubredditPostCompactViewHolder(
                this@RedditPageAdapter, onClickListener,
                ItemSubredditPostCompactBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )


//                    PostViewHolder(
//                        this@RedditPageAdapter, onClickListener,
//                        ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                    )


            else -> throw IllegalArgumentException("Error with view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                is SearchBarViewHolder -> holder.bind(currentItem)
                is PostViewHolder -> holder.bind(currentItem)
                is PostCompactViewHolder -> holder.bind(currentItem)
                is SubredditPostViewHolder -> holder.bind(currentItem)
                is SubredditPostCompactViewHolder -> holder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.dataKind) {
            "search" -> SEARCH
            else -> when (getItem(0)?.isCompact) {
                true -> if (getItem(0)?.isDefault == true) {
                    POST_COMPACT
                } else {
                    SUBREDDIT_POST_COMPACT
                }

                false -> if (getItem(0)?.isDefault == true) {
                    POST
                } else {
                    SUBREDDIT_POST
                }

                else -> ERROR
            }
        }
    }


    interface OnItemClickListener :

        SearchBarViewHolder.OnItemClickListener,
        PostViewHolder.OnItemClickListener,
        PostCompactViewHolder.OnItemClickListener,
        SubredditPostViewHolder.OnItemClickListener,
        SubredditPostCompactViewHolder.OnItemClickListener {

        override fun onSearchSubmit(query: String?, subredditName: String)
        override fun onItemClick(post: RedditChildrenData)
        override fun onVoteClick(post: RedditChildrenData, type: Int)

    }

    companion object {
        private const val SEARCH = 0
        private const val POST = 1
        private const val POST_COMPACT = 2
        private const val SUBREDDIT_POST = 3
        private const val SUBREDDIT_POST_COMPACT = 4
        private const val ERROR = 5

        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<RedditChildrenData>() {
            override fun areItemsTheSame(
                oldItem: RedditChildrenData,
                newItem: RedditChildrenData
            ): Boolean {
                //TODO image duplicating glitch with non image/text only posts (such as vids/gifs and multiple images)
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: RedditChildrenData,
                newItem: RedditChildrenData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}