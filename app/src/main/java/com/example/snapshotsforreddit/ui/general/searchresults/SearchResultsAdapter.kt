package com.example.snapshotsforreddit.ui.general.searchresults

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.PostItemBinding
import com.example.snapshotsforreddit.databinding.PostTextItemBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.example.snapshotsforreddit.util.getShortenedValue

class SearchResultsAdapter(private val onClickListener: OnItemClickListener) : PagingDataAdapter<RedditChildrenObject, RecyclerView.ViewHolder>(SEARCH_RESULT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT_POST -> TextPostViewHolder(PostTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            POST -> PostViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Error with view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                is TextPostViewHolder -> holder.bind(currentItem)
                is PostViewHolder -> holder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.data?.is_self) {
            true -> TEXT_POST
            false -> POST
            else -> ERROR
        }



    }


    inner class PostViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        onClickListener.onItemClick(post)
                    }
                }
            }
            binding.cardUpvoteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        if (post.data?.likes == true) {
                            post.data.likes = null
                            onClickListener.onVoteClick(post, 0, position)
                        } else {
                            post.data?.likes = true
                            onClickListener.onVoteClick(post, 1, position)
                        }

                        notifyItemChanged(position)
                    }
                }
            }
            binding.cardDownvoteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        if (post.data?.likes == false) {
                            post.data.likes = null
                            onClickListener.onVoteClick(post, 0, position)
                        } else {
                            post.data?.likes = false
                            onClickListener.onVoteClick(post, -1, position)
                        }
                        notifyItemChanged(position)
                    }
                }
            }
        }

        @SuppressLint("ResourceAsColor")
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            //TODO FIX CODE HERE : REFORMAT STATEMENTS
            binding.apply {
                //if true, is text only post
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
                    //Subreddit icon
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

                    textviewPostItemSubreddit.text = currentPost.subreddit.toString().replaceFirstChar { it.uppercase() }
                    textviewPostItemTitle.text = currentPost.title
                    textviewPostItemScore.text = getShortenedValue(currentPost.score)
                    textviewPostItemCommentCount.text = getShortenedValue(currentPost.num_comments)
                    val epoch = currentPost.created_utc
                    if (epoch != null) {
                        textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                    }
                    when (currentPost.likes) {
                        true -> {
                            textviewPostItemScore.setTextColor(Color.parseColor(upvoteColor))
                            imageArrow.setImageResource(R.drawable.ic_upvote_arrow)
                        }
                        false -> {
                            textviewPostItemScore.setTextColor(Color.parseColor(downvoteColor))
                            imageArrow.setImageResource(R.drawable.ic_downvote_arrow)
                        }
                        null -> {
                            textviewPostItemScore.setTextColor(coil.base.R.color.androidx_core_secondary_text_default_material_light)
                            imageArrow.setImageResource(R.drawable.ic_up_arrow_null)
                        }
                    }
                }
            }
        }
    }


    inner class TextPostViewHolder(private val binding: PostTextItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        onClickListener.onItemClick(post)
                    }
                }
            }
            binding.cardUpvoteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        if (post.data?.likes == true) {
                            post.data.likes = null
                            onClickListener.onVoteClick(post, 0, position)
                        } else {
                            post.data?.likes = true
                            onClickListener.onVoteClick(post, 1, position)
                        }
                        notifyItemChanged(position)
                    }
                }
            }
            binding.cardDownvoteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        if (post.data?.likes == false) {
                            post.data.likes = null
                            onClickListener.onVoteClick(post, 0, position)
                        } else {
                            post.data?.likes = false
                            onClickListener.onVoteClick(post, -1, position)
                        }
                        notifyItemChanged(position)
                    }
                }
            }
        }


        @SuppressLint("ResourceAsColor")
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
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

                    textviewPostItemSubreddit.text = currentPost.subreddit.toString().replaceFirstChar { it.uppercase() }
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
                    when (currentPost.likes) {
                        true -> {
                            textviewPostItemScore.setTextColor(Color.parseColor(upvoteColor))
                            imageArrow.setImageResource(R.drawable.ic_upvote_arrow)
                        }
                        false -> {
                            textviewPostItemScore.setTextColor(Color.parseColor(downvoteColor))
                            imageArrow.setImageResource(R.drawable.ic_downvote_arrow)
                        }
                        null -> {
                            textviewPostItemScore.setTextColor(coil.base.R.color.androidx_core_secondary_text_default_material_light)
                            imageArrow.setImageResource(R.drawable.ic_up_arrow_null)
                        }
                    }
                }

            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: RedditChildrenObject)
        fun onVoteClick(post: RedditChildrenObject, type: Int, position: Int)
    }

    companion object {
        private const val POST = 0
        private const val TEXT_POST = 1
        private const val ERROR = 2

        private const val removePart = "amp;"
        private const val upvoteColor = "#E24824"
        private const val downvoteColor = "#5250DE"

        private val SEARCH_RESULT_COMPARATOR = object : DiffUtil.ItemCallback<RedditChildrenObject>() {
            override fun areItemsTheSame(oldItem: RedditChildrenObject, newItem: RedditChildrenObject): Boolean {
                //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                //TODO FIX ISSUE WITH IMAGE DUPLICATION
                return oldItem.data.toString() == newItem.data.toString()
            }
            override fun areContentsTheSame(oldItem: RedditChildrenObject, newItem: RedditChildrenObject): Boolean {
                return oldItem == newItem
            }
        }
    }


}