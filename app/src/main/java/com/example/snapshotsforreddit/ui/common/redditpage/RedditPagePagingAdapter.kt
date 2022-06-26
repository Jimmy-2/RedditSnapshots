package com.example.snapshotsforreddit.ui.common.redditpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.data.room.cache.redditpagepost.RedditPagePost
import com.example.snapshotsforreddit.databinding.ItemPostBinding
import com.example.snapshotsforreddit.databinding.ItemPostCompactBinding
import com.example.snapshotsforreddit.databinding.ItemSubredditPostBinding
import com.example.snapshotsforreddit.databinding.ItemSubredditPostCompactBinding
import com.example.snapshotsforreddit.ui.common.test.PostCompactViewHolderTest
import com.example.snapshotsforreddit.ui.common.test.PostViewHolderTest
import com.example.snapshotsforreddit.ui.common.test.SubredditPostCompactViewHolderTest
import com.example.snapshotsforreddit.ui.common.test.SubredditPostViewHolderTest

class RedditPagePagingAdapter(

    private val onItemClick: (RedditPagePost) -> Unit,
    private val onVoteClick: (RedditPagePost, Int) -> Unit,
    private val onMoreClick: (RedditPagePost, Int) -> Unit,
    private val onSubredditClick: (String?) -> Unit,
) : PagingDataAdapter<RedditPagePost, RecyclerView.ViewHolder>(POST_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //private var isCompact = false
//
//        isCompact =  runBlocking { = preferencesFlow.first().isCompactView}
//               viewLifecycleOwner.lifecycleScope.launch {
//                   isCompact =  preferencesFlow.first().isCompactView
//               }

        return when (viewType) {
//            SEARCH -> SearchBarViewHolder(
//
//                ItemSearchBarBinding.inflate(LayoutInflater.from(parent.context), parent, false),
//                onSearchSubmit = { position ->
//
//
//                }
//            )
            POST -> PostViewHolderTest(
                ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onItemClick = { position ->

                },
                onUpvoteClick = { position ->
                    upvoteClicked(position)
                },
                onDownvoteClick = { position ->
                    downvoteClicked(position)

                },
                onMoreClick = { position ->
                    moreClicked(position)
                },
                onSubredditClick = { position ->
                    subredditClicked(position)

                }

            )
            POST_COMPACT -> PostCompactViewHolderTest(

                ItemPostCompactBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onItemClick = { position ->

                },
                onUpvoteClick = { position ->
                    upvoteClicked(position)
                },
                onDownvoteClick = { position ->
                    downvoteClicked(position)

                },
                onMoreClick = { position ->
                    moreClicked(position)
                },
                onSubredditClick = { position ->
                    subredditClicked(position)

                }
            )

            SUBREDDIT_POST -> SubredditPostViewHolderTest(

                ItemSubredditPostBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onItemClick = { position ->

                },
                onUpvoteClick = { position ->
                    upvoteClicked(position)
                },
                onDownvoteClick = { position ->
                    downvoteClicked(position)

                },
                onMoreClick = { position ->
                    moreClicked(position)
                },
            )

            SUBREDDIT_POST_COMPACT -> SubredditPostCompactViewHolderTest(

                ItemSubredditPostCompactBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onItemClick = { position ->

                },
                onUpvoteClick = { position ->
                    upvoteClicked(position)
                },
                onDownvoteClick = { position ->
                    downvoteClicked(position)

                },
                onMoreClick = { position ->
                    moreClicked(position)
                },
            )


            else -> throw IllegalArgumentException("Error with view type")
        }
    }

    private fun upvoteClicked(position: Int) {
        val post = getItem(position)
        if (post != null) {
            if (post.likes == true) {
                post.likes = null
                onVoteClick(post, 0)
            } else {
                post.likes = true
                onVoteClick(post, 1)
            }
        }
    }

    private fun downvoteClicked(position: Int) {
        val post: RedditPagePost? = getItem(position)
        if (post != null) {
            if (post.likes == false) {
                post.likes = null
                onVoteClick(post, 0)
            } else {
                post.likes = false
                onVoteClick(post, -1)
            }

        }
    }

    private fun moreClicked(position: Int) {
        val post = getItem(position)
        if (post != null) {
            onMoreClick(post, 0)
        }
    }

    private fun subredditClicked(position: Int) {
        val post = getItem(position)
        if (post != null) {
            onSubredditClick(post.subreddit)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
//                is SearchBarViewHolderTest -> holder.bind(currentItem)
                is PostViewHolderTest  -> holder.bind(currentItem)
                is PostCompactViewHolderTest  -> holder.bind(currentItem)
                is SubredditPostViewHolderTest  -> holder.bind(currentItem)
                is SubredditPostCompactViewHolderTest  -> holder.bind(currentItem)
            }
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return when (getItem(position)?.dataKind) {
//            "search" -> SEARCH
//            else -> when (getItem(0)?.isCompact) {
//                true -> if (getItem(0)?.isDefault == true) {
//                    POST_COMPACT
//                } else {
//                    SUBREDDIT_POST_COMPACT
//                }
//
//                false -> if (getItem(0)?.isDefault == true) {
//                    POST
//                } else {
//                    SUBREDDIT_POST
//                }
//
//                else -> ERROR
//            }
//        }
//    }

    override fun getItemViewType(position: Int): Int {
        return SUBREDDIT_POST


    }


    companion object {
        private const val SEARCH = 0
        private const val POST = 1
        private const val POST_COMPACT = 2
        private const val SUBREDDIT_POST = 3
        private const val SUBREDDIT_POST_COMPACT = 4
        private const val ERROR = 5

        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<RedditPagePost>() {
            override fun areItemsTheSame(
                oldItem: RedditPagePost,
                newItem: RedditPagePost
            ): Boolean {
                //TODO image duplicating glitch with non image/text only posts (such as vids/gifs and multiple images)
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: RedditPagePost,
                newItem: RedditPagePost
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}