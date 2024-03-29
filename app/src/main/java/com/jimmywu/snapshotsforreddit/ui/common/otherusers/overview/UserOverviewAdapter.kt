package com.jimmywu.snapshotsforreddit.ui.common.otherusers.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jimmywu.snapshotsforreddit.R
import com.jimmywu.snapshotsforreddit.databinding.*
import com.jimmywu.snapshotsforreddit.network.responses.RedditChildrenData
import com.jimmywu.snapshotsforreddit.ui.common.viewholders.PostCompactViewHolder
import com.jimmywu.snapshotsforreddit.ui.common.viewholders.PostViewHolder
import com.jimmywu.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.jimmywu.snapshotsforreddit.util.getShortenedValue

class UserOverviewAdapter(private val onClickListener: OnItemClickListener) :
    PagingDataAdapter<RedditChildrenData, RecyclerView.ViewHolder>(
        POST_COMPARATOR
    ) {
    //TODO: Instead of 1 item layout for a post, have 2 different layouts, 1 for text only post vs image/video/gif post

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COMMENT -> CommentViewHolder(
                ItemAccountCommentBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            POST -> PostViewHolder(
                this@UserOverviewAdapter,
                onClickListener,
                ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                true,
            )
            POST_COMPACT -> PostCompactViewHolder(
                this@UserOverviewAdapter,
                onClickListener,
                ItemPostCompactBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                true
            )
            USER_INFO -> UserInfoViewHolder(
                ItemAccountUserInfoBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            HISTORY -> HistoryViewHolder(
                ItemUserHistoryBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            HEADER -> HeaderViewHolder(
                ItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Error with view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                is CommentViewHolder -> holder.bind(currentItem)
                is PostViewHolder -> holder.bind(currentItem)
                is PostCompactViewHolder -> holder.bind(currentItem)
                is UserInfoViewHolder -> holder.bind(currentItem)
                is HistoryViewHolder -> holder.bind(currentItem)
                is HeaderViewHolder -> holder.bind(currentItem)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.dataKind) {
            "userInfo" -> USER_INFO
            "history" -> HISTORY
            "header" -> HEADER
            else ->
                when (getItem(position)?.name?.take(2)) {
                    "t1" -> COMMENT
                    "t3" -> when (getItem(0)?.isCompact) {
                        true -> POST_COMPACT
                        else -> POST
                    }
                    else -> ERROR
                }
        }
    }

    inner class CommentViewHolder(val binding: ItemAccountCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: RedditChildrenData) {
            binding.apply {
                textviewAccountCommentAuthor.text = post.author
                textviewAccountCommentBody.text = post.body
                textviewAccountCommentPostTitle.text = post.link_title
                textviewAccountCommentPostSubreddit.text = post.subreddit
                textviewUpvoteCount.text = getShortenedValue(post.score)
                when (post.likes) {
                    null -> {
                        imageArrow.setImageResource(R.drawable.ic_up_arrow_null)
                    }
                    true -> {
                        imageArrow.setImageResource(R.drawable.ic_upvote_arrow)
                    }
                    else -> {
                        imageArrow.setImageResource(R.drawable.ic_downvote_arrow)
                    }
                }
                val epoch = post.created_utc
                if (epoch != null) {
                    textviewCommentAge.text = calculateAgeDifferenceLocalDateTime(epoch, 0)
                }
            }
        }
    }


    inner class UserInfoViewHolder(val binding: ItemAccountUserInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutCommentKarma.setOnClickListener { userInfoClicked(0) }
            binding.layoutPostKarma.setOnClickListener { userInfoClicked(1) }
            binding.layoutAccountAge.setOnClickListener { userInfoClicked(2) }
        }

        private fun userInfoClicked(type: Int) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position)
                if (item != null) {
                    onClickListener.onInfoClick(item, type)
                }
            }
        }

        fun bind(post: RedditChildrenData) {
            binding.apply {
                textviewCommentKarma.text =
                    getShortenedValue(post.comment_karma)
                textviewPostKarma.text = getShortenedValue(post.link_karma)
                val epoch = post.user_created_utc
                if (epoch != null) {
                    textviewAccountAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                }
            }
        }
    }

    //USER HISTORY
    inner class HistoryViewHolder(val binding: ItemUserHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.buttonPostsHistory.setOnClickListener {
                navigateToHistory("submitted", "Posts")
            }
            binding.buttonCommentsHistory.setOnClickListener {
                navigateToHistory("comments", "Comments")
            }
            binding.buttonTrophiesHistory.setOnClickListener {
                navigateToHistory(null, "Trophies")
            }


//            binding.root.setOnClickListener {
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    val item = getItem(position)
//                    if (item != null) {
//                        onClickListener.onHistoryClick(item.history_type,item.history_name,item.user_name)
//                    }
//                }
//            }
        }

        private fun navigateToHistory(historyType: String?, historyName: String) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position)
                if (item != null) {
                    onClickListener.onHistoryClick(historyType, historyName, item.user_name)
                }
            }
        }


        fun bind(post: RedditChildrenData) {
            binding.apply {
            }
        }
    }


    inner class HeaderViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: RedditChildrenData) {
            binding.apply {
                textviewHeader.visibility = View.VISIBLE
                textviewHeader.text = "OVERVIEW"
            }
        }
    }

    interface OnItemClickListener : PostCompactViewHolder.OnItemClickListener,
        PostViewHolder.OnItemClickListener {
        fun onInfoClick(infoItem: RedditChildrenData, type: Int)
        fun onHistoryClick(historyType: String?, historyName: String?, userName: String?)
        fun onPostCommentClick(overviewItem: RedditChildrenData, type: Int)
    }


    companion object {
        // View Types
        private const val COMMENT = 0
        private const val POST = 1
        private const val POST_COMPACT = 2
        private const val USER_INFO = 3
        private const val HISTORY = 4
        private const val HEADER = 5
        private const val LOADING = 6
        private const val ERROR = 7


        private val POST_COMPARATOR =
            object : DiffUtil.ItemCallback<RedditChildrenData>() {
                override fun areItemsTheSame(
                    oldItem: RedditChildrenData,
                    newItem: RedditChildrenData
                ): Boolean {
                    //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
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