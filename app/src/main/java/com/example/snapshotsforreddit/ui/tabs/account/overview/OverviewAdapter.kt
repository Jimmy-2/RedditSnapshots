package com.example.snapshotsforreddit.ui.tabs.account.overview

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.*
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.ui.common.viewholders.PostCompactViewHolderTest
import com.example.snapshotsforreddit.ui.common.viewholders.PostViewHolderTest
import com.example.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.example.snapshotsforreddit.util.getShortenedValue

class OverviewAdapter(private val onClickListener: OnItemClickListener) :
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
            POST -> PostViewHolderTest(
                this@OverviewAdapter,
                onClickListener,
                ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                true,
            )
            POST_COMPACT -> PostCompactViewHolderTest(
                this@OverviewAdapter,
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
            DEFAULT -> DefaultViewHolder(
                ItemAccountDefaultBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DEFAULT_TOP -> DefaultViewHolder(
                ItemAccountDefaultBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DEFAULT_BOTTOM -> DefaultViewHolder(
                ItemAccountDefaultBinding.inflate(
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
                is PostViewHolderTest -> holder.bind(currentItem)
                is PostCompactViewHolderTest -> holder.bind(currentItem)
                is UserInfoViewHolder -> holder.bind(currentItem)
                is DefaultViewHolder -> holder.bind(currentItem)
                is HeaderViewHolder -> holder.bind(currentItem)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.dataKind) {
            "userInfo" -> USER_INFO
            "default" -> DEFAULT
            "defaultTop" -> DEFAULT_TOP
            "defaultBottom" -> DEFAULT_BOTTOM
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
    inner class DefaultViewHolder(val binding: ItemAccountDefaultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onClickListener.onHistoryClick(item.history_type,item.history_name,item.user_name)
                    }
                }
            }
        }

        @SuppressLint("ResourceAsColor")
        fun bind(post: RedditChildrenData) {
            binding.apply {
                val leftIcon = post.icon
                when (post.dataKind) {
                    "default" -> {
                        layoutAccountDefault.setBackgroundColor(Color.WHITE)
                        cardDefaultMid.visibility = View.VISIBLE
                        textviewDefaultMid.text = post.history_name
                        leftIcon?.let {
                            textviewDefaultMid.setCompoundDrawablesWithIntrinsicBounds(
                                it, 0, R.drawable.ic_right_arrow, 0
                            )
                            textviewDefaultMid.compoundDrawables[0].setTint(Color.parseColor("#3895e8"))
                        }
                    }
                    "defaultTop" -> {
                        cardDefaultTop.visibility = View.VISIBLE
                        textviewDefaultTop.text = post.history_name
                        leftIcon?.let {
                            textviewDefaultTop.setCompoundDrawablesWithIntrinsicBounds(
                                it, 0, R.drawable.ic_right_arrow, 0
                            )
                            textviewDefaultTop.compoundDrawables[0].setTint(Color.parseColor("#3895e8"))
                        }
                    }
                    "defaultBottom" -> {
                        cardDefaultBottom.visibility = View.VISIBLE
                        textviewDefaultBottom.text = post.history_name
                        leftIcon?.let {
                            textviewDefaultBottom.setCompoundDrawablesWithIntrinsicBounds(
                                it, 0, R.drawable.ic_right_arrow, 0
                            )
                            textviewDefaultBottom.compoundDrawables[0].setTint(
                                Color.parseColor(
                                    "#3895e8"
                                )
                            )
                        }
                    }
                }

            }
        }
    }


    inner class HeaderViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: RedditChildrenData) {
            binding.apply {
                textviewHeaderOverview.visibility = View.VISIBLE
                textviewHeaderOverview.text = "OVERVIEW"
            }
        }
    }

    interface OnItemClickListener : PostCompactViewHolderTest.OnItemClickListener, PostViewHolderTest.OnItemClickListener {
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
        private const val DEFAULT = 4
        private const val DEFAULT_TOP = 5
        private const val DEFAULT_BOTTOM = 6
        private const val HEADER = 7
        private const val LOADING = 8
        private const val ERROR = 9


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