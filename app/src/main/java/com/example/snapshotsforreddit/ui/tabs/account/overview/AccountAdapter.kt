package com.example.snapshotsforreddit.ui.tabs.account.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.*
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import java.time.*

class AccountAdapter(private val onClickListener: OnItemClickListener) :
    PagingDataAdapter<RedditChildrenObject, RecyclerView.ViewHolder>(
        POST_COMPARATOR
    ) {
    //TODO: Instead of 1 item layout for a post, have 2 different layouts, 1 for text only post vs image/video/gif post

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            COMMENT -> CommentViewHolder(
                AccountCommentItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            POST -> PostViewHolder(
                PostItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            TEXT_POST -> TextPostViewHolder(
                PostTextItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            USER_INFO -> UserInfoViewHolder(
                AccountUserInfoItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DEFAULT -> DefaultViewHolder(
                AccountDefaultItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DEFAULT_TOP -> DefaultTopViewHolder(
                AccountDefaultTopItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            DEFAULT_BOTTOM -> DefaultBottomViewHolder(
                AccountDefaultBottomItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            HEADER -> HeaderViewHolder(
                HeaderItemBinding.inflate(
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
                is TextPostViewHolder -> holder.bind(currentItem)
                is UserInfoViewHolder -> holder.bind(currentItem)
                is DefaultViewHolder -> holder.bind(currentItem)
                is DefaultTopViewHolder -> holder.bind(currentItem)
                is DefaultBottomViewHolder -> holder.bind(currentItem)
                is HeaderViewHolder -> holder.bind(currentItem)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.kind) {
            "t1" -> COMMENT
            "t3" -> return when (getItem(position)?.data?.is_self) {
                true -> TEXT_POST
                else -> POST
            }
            "userInfo" -> USER_INFO
            "default" -> DEFAULT
            "defaultTop" -> DEFAULT_TOP
            "defaultBottom" -> DEFAULT_BOTTOM
            "header" -> HEADER
            else -> ERROR
        }

    }


    inner class CommentViewHolder(val binding: AccountCommentItemBinding) :
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

    inner class PostViewHolder(val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            val removePart = "amp;"
            //TODO FIX CODE HERE : REFORMAT STATEMENTS
            binding.apply {
                //TODO REDO THIS TO ALLOW GIFS AND VIDEOS
                if (currentPost != null) {
                    if (currentPost.preview?.images?.get(0)?.source?.url != null) {
                        val imageUrl =
                            currentPost.preview.images[0].source!!.url?.replace(removePart, "")
                        Glide.with(itemView)
                            .load(imageUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_error)
                            .into(imageviewPostItem)
                    }
                    val currIconUrl = currentPost.sr_detail?.community_icon.toString()
                    val iconUrl: String =
                        if (currentPost.sr_detail?.community_icon == null && currentPost.sr_detail?.icon_img == null) {
                            ""
                        } else if (currIconUrl == "null") {
                            currentPost.sr_detail.icon_img!!.replace(removePart, "")
                        } else {
                            currIconUrl.replace(removePart, "")
                        }
                    Glide.with(itemView)
                        .load(iconUrl)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageviewSubredditIcon)

                    textviewPostItemSubreddit.text =
                        currentPost.subreddit.toString().replaceFirstChar { it.uppercase() }
                    textviewPostItemTitle.text = currentPost.title
                    textviewPostItemScore.text = getShortenedValue(currentPost.score)
                    textviewPostItemCommentCount.text = getShortenedValue(currentPost.num_comments)
                    val epoch = currentPost.created_utc
                    if (epoch != null) {
                        textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                    }

                }

            }
        }

    }

    inner class TextPostViewHolder(private val binding: PostTextItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            val removePart = "amp;"
            binding.apply {
                if (currentPost != null) {
                    val currIconUrl = currentPost.sr_detail?.community_icon.toString()
                    val iconUrl: String =
                        if (currentPost.sr_detail?.community_icon == null && currentPost.sr_detail?.icon_img == null) {
                            ""
                        } else if (currIconUrl == "null") {
                            currentPost.sr_detail.icon_img!!.replace(removePart, "")
                        } else {
                            currIconUrl.replace(removePart, "")
                        }
                    Glide.with(itemView)
                        .load(iconUrl)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageviewSubredditIcon)

                    textviewPostItemSubreddit.text =
                        currentPost.subreddit.toString().replaceFirstChar { it.uppercase() }
                    textviewPostItemTitle.text = currentPost.title
                    if (currentPost.selftext == "") {
                        textviewPostItemText.visibility = View.GONE
                    } else {
                        textviewPostItemText.text = currentPost.selftext
                    }

                    textviewPostItemScore.text = getShortenedValue(currentPost.score)
                    textviewPostItemCommentCount.text = getShortenedValue(currentPost.num_comments)
                    val epoch = currentPost.created_utc
                    if (epoch != null) {
                        textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                    }
                }

            }
        }
    }


    inner class UserInfoViewHolder(val binding: AccountUserInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
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

    inner class DefaultViewHolder(val binding: AccountDefaultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onClickListener.onHistoryClick(item.data?.permalink,
                            item.data?.userInfo?.name
                        )
                    }
                }
            }
        }
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            binding.apply {
                if (currentPost != null) {
                    textviewDefault.text = currentPost.body
                }
            }
        }
    }

    inner class DefaultTopViewHolder(val binding: AccountDefaultTopItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onClickListener.onHistoryClick(item.data?.permalink,item.data?.userInfo?.name)
                    }
                }
            }
        }
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            binding.apply {
                if (currentPost != null) {
                    textviewDefault.text = currentPost.body
                }
            }
        }
    }

    inner class DefaultBottomViewHolder(val binding: AccountDefaultBottomItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onClickListener.onHistoryClick(item.data?.permalink,item.data?.userInfo?.name)
                    }
                }
            }
        }
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            binding.apply {
                if (currentPost != null) {
                    textviewDefault.text = currentPost.body

                }
            }
        }
    }

    inner class HeaderViewHolder(val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditChildrenObject) {
            binding.apply {
                textviewHeader.text = "OVERVIEW"
            }
        }
    }

    interface OnItemClickListener {
        fun onInfoClick(infoItem: RedditChildrenObject, type: Int)

        fun onHistoryClick(historyType: String?, username: String?)

        fun onPostCommentClick(overviewItem: RedditChildrenObject, type: Int)


    }


    companion object {
        // View Types
        private val COMMENT = 0
        private val POST = 1
        private val TEXT_POST = 2
        private val USER_INFO = 3
        private val DEFAULT = 4
        private val DEFAULT_TOP = 5
        private val DEFAULT_BOTTOM = 6
        private val HEADER = 7
        private val LOADING = 8
        private val ERROR = 9


        private val POST_COMPARATOR =
            object : DiffUtil.ItemCallback<RedditChildrenObject>() {
                override fun areItemsTheSame(
                    oldItem: RedditChildrenObject,
                    newItem: RedditChildrenObject
                ): Boolean {

                    //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                    return oldItem.data.toString() == newItem.data.toString()
                }

                override fun areContentsTheSame(
                    oldItem: RedditChildrenObject,
                    newItem: RedditChildrenObject
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }

    private fun getShortenedValue(value: Int?): String {
        return when {
            value == null -> {
                ""
            }
            value < 1000 -> {
                value.toString()
            }
            value < 1000000 -> {
                String.format("%.1f", (value.toDouble() / 1000)) + "K"
            }
            else -> {
                String.format("%.1f", (value.toDouble() / 1000000)) + "M"
            }
        }
    }

    private fun calculateAgeDifferenceLocalDateTime(epoch: Long, type: Int): String {
        val createdOn = epoch.let {
            Instant.ofEpochMilli(it * 1000).atZone(ZoneId.systemDefault()).toLocalDate()
        }
        val ageMY = Period.between(createdOn, LocalDate.now())
        return when {
            //if both years and months == 0, we measure account age by days, hours, and minutes instead
            ageMY.years == 0 && ageMY.months == 0 -> {
                val createdOn = epoch.let {
                    Instant.ofEpochMilli(it * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime()
                }
                //Days, hours, minutes, and seconds
                val ageDHMS = Duration.between(createdOn, LocalDateTime.now())
                return when {
                    ageDHMS.toHours() < 24 -> {
                        if (ageDHMS.toHours() < 1) {
                            if (ageDHMS.toMinutes() < 1) {
                                return "${ageDHMS.seconds}s"
                            } else {
                                return "${ageDHMS.toMinutes()}m"
                            }
                        } else {
                            return "${ageDHMS.toHours()}h"
                        }
                    }
                    else -> "${ageDHMS.toHours() / 24}d"
                }
            }
            ageMY.years == 0 -> "${ageMY.months}mo"
            ageMY.months == 0 -> "${ageMY.years}y "
            //type 1 = account age, type 0 = comments
            else -> if (type == 1) {
                "${ageMY.years}y ${ageMY.months}mo"
            } else {
                //comments older than or equal to 1 year are formatted as decimals
                "${
                    String.format(
                        "%.1f",
                        ((ageMY.years * 12.toDouble()) + ageMY.months.toDouble()) / 12
                    )
                }y"
            }
        }
    }


}