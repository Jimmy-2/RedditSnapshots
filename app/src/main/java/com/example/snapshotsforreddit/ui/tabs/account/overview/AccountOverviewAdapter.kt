package com.example.snapshotsforreddit.ui.tabs.account.overview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.*
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.ui.general.PostViewHolder
import com.example.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.example.snapshotsforreddit.util.getShortenedValue

class AccountOverviewAdapter(private val onClickListener: OnItemClickListener) :
    PagingDataAdapter<RedditChildrenObject, RecyclerView.ViewHolder>(
        POST_COMPARATOR
    ) {
    //TODO: Instead of 1 item layout for a post, have 2 different layouts, 1 for text only post vs image/video/gif post

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COMMENT -> CommentViewHolder(ItemAccountCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            POST -> PostViewHolder(this@AccountOverviewAdapter, onClickListener, ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            USER_INFO -> UserInfoViewHolder(ItemAccountUserInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            DEFAULT -> DefaultViewHolder(ItemAccountDefaultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            HEADER -> HeaderViewHolder(ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Error with view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                is CommentViewHolder -> holder.bind(currentItem)
                is PostViewHolder -> holder.bind(currentItem)
                is UserInfoViewHolder -> holder.bind(currentItem)
                is DefaultViewHolder -> holder.bind(currentItem)
                is HeaderViewHolder -> holder.bind(currentItem)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.kind) {
            "t1" -> COMMENT
            "t3" -> POST
            "userInfo" -> USER_INFO
            "default" -> DEFAULT
            "defaultTop" -> DEFAULT
            "defaultBottom" -> DEFAULT
            "header" -> HEADER
            else -> ERROR
        }

    }
    inner class CommentViewHolder(val binding: ItemAccountCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            binding.apply {
                textviewAccountCommentAuthor.text = currentPost?.author
                textviewAccountCommentBody.text = currentPost?.body
                textviewAccountCommentPostTitle.text = currentPost?.link_title
                textviewAccountCommentPostSubreddit.text = currentPost?.subreddit
                textviewUpvoteCount.text = getShortenedValue(currentPost?.score)
                when {
                    currentPost?.likes == null -> {
                        imageArrow.setImageResource(R.drawable.ic_up_arrow_null)
                    }
                    currentPost.likes == true -> {
                        imageArrow.setImageResource(R.drawable.ic_upvote_arrow)
                    }
                    else -> {
                        imageArrow.setImageResource(R.drawable.ic_downvote_arrow)
                    }
                }
                val epoch = currentPost?.created_utc
                if (epoch != null) {
                    textviewCommentAge.text = calculateAgeDifferenceLocalDateTime(epoch, 0)
                }
            }
        }
    }




    inner class UserInfoViewHolder(val binding: ItemAccountUserInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.defaults
            binding.apply {
                if (currentPost != null) {
                    textviewCommentKarma.text =
                        getShortenedValue(currentPost.userInfo?.comment_karma)
                    textviewPostKarma.text = getShortenedValue(currentPost.userInfo?.link_karma)
                    val epoch = currentPost.userInfo?.created_utc

                    if (epoch != null) {
                        textviewAccountAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                    }
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
                        onClickListener.onHistoryClick(
                            item.defaults?.type,
                            item.defaults?.userInfo?.name
                        )
                    }
                }
            }
        }
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.defaults
            binding.apply {
                if (currentPost != null) {
                    println(postObject.kind)
                    when(postObject.kind) {

                        "default" -> {
                            layoutAccountDefault.setBackgroundColor(Color.WHITE)
                            cardDefaultMid.visibility = View.VISIBLE
                            textviewDefaultMid.text = currentPost.text
                        }
                        "defaultTop" -> {
                            cardDefaultTop.visibility = View.VISIBLE
                            textviewDefaultTop.text = currentPost.text
                        }
                        "defaultBottom" -> {
                            cardDefaultBottom.visibility = View.VISIBLE
                            textviewDefaultBottom.text = currentPost.text
                        }
                    }

                }
            }
        }
    }



    inner class HeaderViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditChildrenObject) {
            binding.apply {
                textviewHeaderOverview.visibility = View.VISIBLE
                textviewHeaderOverview.text = "OVERVIEW"
            }
        }
    }

    interface OnItemClickListener : PostViewHolder.OnItemClickListener {
        fun onInfoClick(infoItem: RedditChildrenObject, type: Int)
        fun onHistoryClick(historyType: String?, username: String?)
        fun onPostCommentClick(overviewItem: RedditChildrenObject, type: Int)
    }


    companion object {
        // View Types
        private val COMMENT = 0
        private val POST = 1
        private val USER_INFO = 3
        private val DEFAULT = 4
        private val DEFAULT_TOP = 5
        private val DEFAULT_BOTTOM = 6
        private val HEADER = 7
        private val LOADING = 8
        private val ERROR = 9

        const val removePart = "amp;"

        private val POST_COMPARATOR =
            object : DiffUtil.ItemCallback<RedditChildrenObject>() {
                override fun areItemsTheSame(oldItem: RedditChildrenObject, newItem: RedditChildrenObject): Boolean {
                    //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                    return oldItem.data.toString() == newItem.data.toString()
                }
                override fun areContentsTheSame(oldItem: RedditChildrenObject, newItem: RedditChildrenObject): Boolean {
                    return oldItem == newItem
                }

            }
    }




}